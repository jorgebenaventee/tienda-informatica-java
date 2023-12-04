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

/**
 * Implementación del servicio para gestionar operaciones relacionadas con proveedores.
 *
 * @Service Indica que esta clase es un servicio de Spring y puede ser gestionada por el contenedor de Spring.
 * @Slf4j Habilita el registro de eventos utilizando la anotación SLF4J.
 * @CacheConfig Configura el nombre de la caché para las operaciones de proveedores.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "suppliers")
public class SupplierServiceImpl implements SupplierService {

    /**
     * Repositorio para acceder a datos de proveedores.
     */
    private final SupplierRepository supplierRepository;

    /**
     * Mapper para convertir entre DTOs y entidades de proveedores.
     */
    private final SupplierMapper supplierMapper;

    /**
     * Servicio para operaciones relacionadas con categorías.
     */
    private final CategoryService categoryService;

    /**
     * Configuración WebSocket para notificaciones.
     */
    private final WebSocketConfig webSocketConfig;

    /**
     * Mapper para convertir entre entidades de proveedores y DTOs de notificaciones de proveedores.
     */
    private final SuppliersNotificationMapper suppliersNotificationMapper;

    /**
     * ObjectMapper para la manipulación de objetos JSON.
     */
    private final ObjectMapper mapper;

    /**
     * Manejador de WebSocket para enviar notificaciones.
     */
    private WebSocketHandler webSocketService;

    /**
     * Constructor que inicializa las dependencias del servicio de proveedores.
     *
     * @param supplierRepository          Repositorio de proveedores.
     * @param supplierMapper              Mapper de proveedores.
     * @param categoryService             Servicio de categorías.
     * @param webSocketConfig             Configuración WebSocket.
     * @param suppliersNotificationMapper Mapper de notificaciones de proveedores.
     * @param mapper                      ObjectMapper para JSON.
     */
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

    /**
     * Obtiene todos los proveedores con opciones de filtrado y paginación.
     *
     * @param category  Categoría del proveedor (opcional).
     * @param name      Nombre del proveedor (opcional).
     * @param contact   Contacto del proveedor (opcional).
     * @param isDeleted Indica si el proveedor está eliminado (opcional).
     * @param pageable  Información de paginación.
     * @return Página de DTOs de proveedores.
     */
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


    /**
     * Obtiene un proveedor por su UUID, utilizando la caché.
     *
     * @param id UUID del proveedor.
     * @return DTO del proveedor encontrado.
     */
    @Override
    @Cacheable(key = "#id")
    public SupplierResponseDto findByUUID(String id) {
        UUID uuid = getUUID(id);
        return supplierRepository.findById(uuid)
                .map(supplierMapper::toSupplierDto)
                .orElseThrow(() -> new SupplierNotFound(uuid));
    }

    /**
     * Obtiene un proveedor por su nombre.
     *
     * @param name Nombre del proveedor.
     * @return DTO del proveedor encontrado.
     */
    @Override
    public SupplierResponseDto findByName(String name) {
        return supplierRepository.findByNameEqualsIgnoreCase(name)
                .map(supplierMapper::toSupplierDto)
                .orElseThrow(() -> new SupplierNotFound(name));
    }

    /**
     * Convierte una cadena UUID en un objeto UUID para comprobar si la cadena tiene
     * un formato válido de UUID, de lo contrario, se lanza una excepción
     * IllegalArgumentException indicando que la cadena no es una UUID válida.
     *
     * @param uuid Cadena que representa la UUID a convertir.
     * @return Objeto UUID generado a partir de la cadena.
     * @throws SupplierBadRequest Si la cadena no tiene un formato válido de UUID,
     *                            se lanza esta excepción con un mensaje detallado.
     */
    public UUID getUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new SupplierBadRequest(uuid + " is not a valid UUID");
        }
    }

    /**
     * Guarda un nuevo proveedor y envía una notificación de creación.
     *
     * @param supplierCreateDto DTO con la información del proveedor a crear.
     * @return DTO del proveedor creado.
     */
    @Transactional
    @Override
    @CachePut(key = "#result.id")
    public SupplierResponseDto save(SupplierCreateDto supplierCreateDto) {
        categoryService.findById(supplierCreateDto.category().getUuid());
        var supplierToSave = supplierMapper.toSupplier(supplierCreateDto);
        sendNotification(Notification.Tipo.CREATE, supplierToSave);
        return supplierMapper.toSupplierDto(supplierRepository.save(supplierToSave));
    }

    /**
     * Actualiza un proveedor existente y envía una notificación de actualización.
     *
     * @param supplierUpdateDto DTO con la información de actualización del proveedor.
     * @param idSupplier UUID del proveedor a actualizar.
     * @return DTO del proveedor actualizado.
     * @throws SupplierNotFound Si el proveedor no existe.
     */
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

    /**
     * Elimina un proveedor por su UUID y envía una notificación de eliminación.
     *
     * @param idSupplier UUID del proveedor a eliminar.
     * @throws SupplierNotFound Si el proveedor no existe.
     */
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

    /**
     * Envía una notificación a través de WebSocket sobre la operación realizada en el proveedor.
     *
     * @param tipo Tipo de notificación (CREATE, UPDATE, DELETE).
     * @param data Datos del proveedor para la notificación.
     */
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

    /**
     * Configura el servicio WebSocket para pruebas unitarias.
     *
     * @param webSocketHandlerMock Implementación de WebSocketHandler para pruebas unitarias.
     */
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }

}

