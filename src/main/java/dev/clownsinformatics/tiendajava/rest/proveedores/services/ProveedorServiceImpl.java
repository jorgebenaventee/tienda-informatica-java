package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorBadRequest;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.ProveedorRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProveedoresNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ProveedoresNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@CacheConfig(cacheNames = "proveedores")
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;
    private final CategoryService categoryService;
    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketService;
    private final ProveedoresNotificationMapper proveedoresNotificationMapper;
    private final ObjectMapper mapper;

    @Autowired
    public ProveedorServiceImpl(ProveedorRepository proveedoresRepository, ProveedorMapper proveedorMapper, CategoryService categoryService, WebSocketConfig webSocketConfig, ProveedoresNotificationMapper proveedoresNotificationMapper, ObjectMapper mapper) {
        this.proveedorRepository = proveedoresRepository;
        this.proveedorMapper = proveedorMapper;
        this.categoryService = categoryService;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketProveedorHandler();
        this.proveedoresNotificationMapper = proveedoresNotificationMapper;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Proveedor> findAll(String name, String address) {
        if ((name == null || name.isEmpty()) && (address == null || address.isEmpty())) {
            return proveedorRepository.findAll();
        }
        if ((name != null && !name.isEmpty()) && (address == null || address.isEmpty())) {
            return proveedorRepository.getByNameContainingIgnoreCase(name);
        }
        if (name == null || name.isEmpty()) {
            return proveedorRepository.getByAddressContainingIgnoreCase(address);
        }
        return proveedorRepository.getByNameAndAddressContainingIgnoreCase(name, address);
    }


    @Override
    @Cacheable(key = "#idProveedor")
    public Proveedor findByUUID(String idProveedor) {
        try {
            UUID uuid = UUID.fromString(idProveedor);
            return proveedorRepository.getByIdProveedor(uuid).orElseThrow(
                    () -> new ProveedorNotFound(uuid)
            );
        } catch (IllegalArgumentException e) {
            throw new ProveedorBadRequest(idProveedor + " no es un UUID válido");
        }
    }

    @Transactional
    @Override
    @CachePut(key = "#result.idProveedor")
    public Proveedor save(ProveedorCreateDto proveedorCreateDto) {
        categoryService.findById(proveedorCreateDto.category().getUuid());
        Proveedor proveedorToSave = proveedorMapper.toProveedor(proveedorCreateDto, UUID.randomUUID());
        sendNotification(Notification.Tipo.CREATE, proveedorToSave);
        return proveedorRepository.save(proveedorToSave);
    }

    @Override
    @CachePut(key = "#idProveedor")
    public Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor) {
        Proveedor proveedorActual = findByUUID(idProveedor);
        sendNotification(Notification.Tipo.UPDATE, proveedorActual);
        return proveedorRepository.save(proveedorMapper.toProveedor(proveedorUpdateDto, proveedorActual));
    }

    @Transactional
    @Override
    @CacheEvict(key = "#idProveedor")
    public void deleteByUUID(String idProveedor) {
        Proveedor proveedor = findByUUID(idProveedor);
        proveedorRepository.deleteByIdProveedor(UUID.fromString(idProveedor));
        sendNotification(Notification.Tipo.DELETE, proveedor);
    }

    void sendNotification(Notification.Tipo tipo, Proveedor data) {
        if (webSocketService == null) {
            log.warn("No se ha configurado el servicio de websockets");
            webSocketService = this.webSocketConfig.webSocketProveedorHandler();
        }
        try {
            Notification<ProveedoresNotificationDto> notificacion = new Notification<>(
                    "PROVEEDORES",
                    tipo,
                    proveedoresNotificationMapper.toProveedoresNotificationDto(data),
                    LocalDate.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));
            log.info("Enviando notificación mensaje..");

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }

    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }

}

