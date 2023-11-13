package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorBadRequest;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.ProveedorRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProveedoresNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ProveedoresNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Optional;
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
    public Page<ProveedorResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Pageable pageable) {
        Specification<Proveedor> specProveedorCategory = (root, query, criteriaBuilder) ->
                category.map(c -> {
                    Join<Proveedor, Category> categoriaJoin = root.join("category");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("name")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> specProveedorName = (root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Proveedor> specProveedorContact = (root, query, criteriaBuilder) ->
                contact.map(c -> criteriaBuilder.equal(root.get("contact"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> spec = Specification.where(specProveedorCategory).and(specProveedorName).and(specProveedorContact);
        return proveedorRepository.findAll(spec, pageable).map(proveedorMapper::toProveedorDto);
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
            throw new ProveedorBadRequest(idProveedor + " is not a valid UUID");
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
            log.warn("WebSocket service is not configured");
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
            log.info("Sending notification..");

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error while sending the message", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error while parsing the notification", e);
        }
    }

    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }

}

