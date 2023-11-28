package dev.clownsinformatics.tiendajava.rest.suppliers.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.exceptions.SupplierBadRequest;
import dev.clownsinformatics.tiendajava.rest.suppliers.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.suppliers.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.suppliers.repositories.SupplierRepository;
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

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final CategoryService categoryService;
    private final WebSocketConfig webSocketConfig;
    private final SuppliersNotificationMapper suppliersNotificationMapper;
    private final ObjectMapper mapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper, CategoryService categoryService, WebSocketConfig webSocketConfig, SuppliersNotificationMapper suppliersNotificationMapper, ObjectMapper mapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.categoryService = categoryService;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketSupplierHandler();
        this.suppliersNotificationMapper = suppliersNotificationMapper;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Page<SupplierResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Optional<Boolean> isDeleted, Pageable pageable) {
        Specification<Supplier> specSupplierCategory = (root, query, criteriaBuilder) ->
                category.map(c -> {
                    Join<Supplier, Category> categoriaJoin = root.join("category");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("name")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Supplier> specSupplierName = (root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Supplier> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Supplier> specSupplierContact = (root, query, criteriaBuilder) ->
                contact.map(c -> criteriaBuilder.equal(root.get("contact"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Supplier> spec = Specification.where(specSupplierCategory).and(specSupplierName).and(specIsDeleted).and(specSupplierContact);
        return supplierRepository.findAll(spec, pageable).map(supplierMapper::toSupplierDto);
    }


    @Override
    @Cacheable(key = "#id")
    public SupplierResponseDto findByUUID(String id) {
        UUID uuid = getUUID(id);
        return supplierRepository.findById(uuid)
                .map(supplierMapper::toSupplierDto)
                .orElseThrow(() -> new SupplierNotFound(uuid));
    }

    @Override
    public SupplierResponseDto findByName(String name) {
        return supplierRepository.findByNameEqualsIgnoreCase(name)
                .map(supplierMapper::toSupplierDto)
                .orElseThrow(() -> new SupplierNotFound(name));
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
    public SupplierResponseDto save(SupplierCreateDto supplierCreateDto) {
        categoryService.findById(supplierCreateDto.category().getUuid());
        var supplierToSave = supplierMapper.toSupplier(supplierCreateDto);
        sendNotification(Notification.Tipo.CREATE, supplierToSave);
        return supplierMapper.toSupplierDto(supplierRepository.save(supplierToSave));
    }


    @Override
    @CachePut(key = "#idSupplier")
    public SupplierResponseDto update(SupplierUpdateDto supplierUpdateDto, String idSupplier) {
        UUID uuid = getUUID(idSupplier);
        var supplierToUpdate = supplierRepository.findById(uuid)
                .orElseThrow(() -> new SupplierNotFound(getUUID(idSupplier)));
        var supplier = supplierMapper.toSupplier(supplierUpdateDto, supplierToUpdate);
        sendNotification(Notification.Tipo.UPDATE, supplier);
        return supplierMapper.toSupplierDto(supplierRepository.save(supplier));
    }

    @Transactional
    @Override
    @CacheEvict(key = "#idSupplier")
    public void deleteByUUID(String idSupplier) {
        UUID uuid = getUUID(idSupplier);
        Supplier supplierToDelete = supplierRepository.findById(uuid)
                .orElseThrow(() -> new SupplierNotFound(uuid));
        sendNotification(Notification.Tipo.DELETE, supplierToDelete);
        supplierRepository.deleteById(uuid);
    }

    void sendNotification(Notification.Tipo tipo, Supplier data) {
        if (webSocketService == null) {
            log.warn("WebSocket service is not configured");
            webSocketService = this.webSocketConfig.webSocketSupplierHandler();
        }
        try {
            Notification<SuppliersNotificationDto> notificacion = new Notification<>(
                    "SUPPLIERS",
                    tipo,
                    suppliersNotificationMapper.toSupplierNotificationDto(data),
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

