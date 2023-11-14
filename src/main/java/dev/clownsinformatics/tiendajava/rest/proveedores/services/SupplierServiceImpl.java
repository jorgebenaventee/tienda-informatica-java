package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.SupplierBadRequest;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.SupplierRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.SuppliersNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.SuppliersNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@CacheConfig(cacheNames = "suppliers")
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository proveedorRepository;
    private final SupplierMapper proveedorMapper;
    private final CategoryService categoryService;
    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketService;
    private final SuppliersNotificationMapper proveedoresNotificationMapper;
    private final ObjectMapper mapper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository proveedoresRepository, SupplierMapper proveedorMapper, CategoryService categoryService, WebSocketConfig webSocketConfig, SuppliersNotificationMapper proveedoresNotificationMapper, ObjectMapper mapper) {
        this.proveedorRepository = proveedoresRepository;
        this.proveedorMapper = proveedorMapper;
        this.categoryService = categoryService;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketProveedorHandler();
        this.proveedoresNotificationMapper = proveedoresNotificationMapper;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Page<SupplierResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Pageable pageable) {
        Specification<Supplier> specProveedorCategory = (root, query, criteriaBuilder) ->
                category.map(c -> {
                    Join<Supplier, Category> categoriaJoin = root.join("category");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("name")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Supplier> specProveedorName = (root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Supplier> specProveedorContact = (root, query, criteriaBuilder) ->
                contact.map(c -> criteriaBuilder.equal(root.get("contact"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Supplier> spec = Specification.where(specProveedorCategory).and(specProveedorName).and(specProveedorContact);
        return proveedorRepository.findAll(spec, pageable).map(proveedorMapper::toProveedorDto);
    }


    @Override
    @Cacheable(key = "#idProveedor")
    public SupplierResponseDto findByUUID(String idProveedor) {
        UUID uuid = getUUID(idProveedor);
        return proveedorRepository.findById(uuid)
                .map(proveedorMapper::toProveedorDto)
                .orElseThrow(() -> new SupplierNotFound(uuid));
    }

    public UUID getUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new SupplierBadRequest(uuid + " is not a valid UUID");
        }
    }


    @Transactional
    @Override
    @CachePut(key = "#result.id")
    public SupplierResponseDto save(SupplierCreateDto proveedorCreateDto) {
        categoryService.findById(proveedorCreateDto.category().getUuid());
        var proveedorToSave = proveedorMapper.toProveedor(proveedorCreateDto);
        sendNotification(Notification.Tipo.CREATE, proveedorToSave);
        return proveedorMapper.toProveedorDto(proveedorRepository.save(proveedorToSave));
    }


    @Override
    @CachePut(key = "#idProveedor")
    public SupplierResponseDto update(SupplierUpdateDto proveedorUpdateDto, String idProveedor) {
        UUID uuid = getUUID(idProveedor);
        var proveedorToUpdate = proveedorRepository.findById(uuid)
                .orElseThrow(() -> new SupplierNotFound(getUUID(idProveedor)));
        var proveedor = proveedorMapper.toProveedor(proveedorUpdateDto, proveedorToUpdate);
        sendNotification(Notification.Tipo.UPDATE, proveedor);
        return proveedorMapper.toProveedorDto(proveedorRepository.save(proveedor));
    }

    @Transactional
    @Override
    @CacheEvict(key = "#idProveedor")
    public void deleteByUUID(String idProveedor) {
        UUID uuid = getUUID(idProveedor);
        Supplier proveedorToDelete = proveedorRepository.findById(uuid)
                .orElseThrow(() -> new SupplierNotFound(uuid));
        sendNotification(Notification.Tipo.DELETE, proveedorToDelete);
        proveedorRepository.deleteById(uuid);
    }

    void sendNotification(Notification.Tipo tipo, Supplier data) {
        if (webSocketService == null) {
            log.warn("WebSocket service is not configured");
            webSocketService = this.webSocketConfig.webSocketProveedorHandler();
        }
        try {
            Notification<SuppliersNotificationDto> notificacion = new Notification<>(
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

