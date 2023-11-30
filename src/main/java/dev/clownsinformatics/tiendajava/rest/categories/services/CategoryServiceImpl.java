package dev.clownsinformatics.tiendajava.rest.categories.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryConflict;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryNotFound;
import dev.clownsinformatics.tiendajava.rest.categories.mappers.CategoryMapper;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.CategoryNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.CategoryNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de {@link CategoryService} que gestiona las operaciones relacionadas con las categorías de productos.
 * Esta clase proporciona métodos para buscar, crear, actualizar y eliminar categorías, así como notificar cambios mediante WebSocket.
 *
 * @version 1.0
 * @since 2023-11-28
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Category not found";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = new CategoryMapper();

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final CategoryNotificationMapper categoryNotificationMapper;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, WebSocketConfig webSocketConfig, ObjectMapper mapper, CategoryNotificationMapper categoryNotificationMapper) {
        this.categoryRepository = categoryRepository;
        this.webSocketConfig = webSocketConfig;
        this.mapper = mapper;
        this.categoryNotificationMapper = categoryNotificationMapper;
        webSocketHandler = webSocketConfig.webSocketCategoryHandler();
    }

    /**
     * Obtiene todas las categorías disponibles con opciones de filtrado y paginación.
     *
     * @param name      Opcional. Nombre de la categoría para filtrar.
     * @param isDeleted Opcional. Indica si se deben incluir categorías eliminadas.
     * @param pageable  Información de paginación.
     * @return Página de categorías que cumplen con los criterios de búsqueda y paginación.
     */
    @Override
    public Page<Category> findAll(Optional<String> name, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Getting all categories with name: {}", name);
        Specification<Category> specName = (root, criteriaQuery, criteriaBuilder) ->
                name.map(value -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Category> specIsDeleted = (root, criteriaQuery, criteriaBuilder) ->
                isDeleted.map(value -> criteriaBuilder.equal(root.get("isDeleted"), value))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Category> spec = Specification.where(specName)
                .and(specIsDeleted);
        return categoryRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene una categoría por su identificador único.
     *
     * @param id Identificador único de la categoría.
     * @return La categoría encontrada.
     * @throws CategoryNotFound Excepción lanzada si la categoría no se encuentra.
     */
    @Override
    @Cacheable
    public Category findById(UUID id) {
        log.info("Getting category with id: {}", id);
        return categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
    }

    /**
     * Obtiene una categoría por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param name Nombre de la categoría.
     * @return La categoría encontrada.
     * @throws CategoryNotFound Excepción lanzada si la categoría no se encuentra.
     */
    @Override
    @Cacheable
    public Category findByName(String name) {
        log.info("Getting category with name: {}", name);
        return categoryRepository.findByNameEqualsIgnoreCase(name).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
    }

    /**
     * Guarda una nueva categoría en el sistema.
     *
     * @param category DTO con la información de la nueva categoría.
     * @return La categoría guardada.
     * @throws CategoryConflict Excepción lanzada si ya existe una categoría con el mismo nombre.
     */
    @Override
    @Cacheable
    public Category save(CategoryResponseDto category) {
        log.info("Saving category");
        categoryRepository.findByName(category.name()).ifPresent(c -> {
            throw new CategoryConflict("Category already exists");
        });
        onChange(Notification.Tipo.CREATE, categoryMapper.toCategory(category));
        return categoryRepository.save(categoryMapper.toCategory(category));
    }

    /**
     * Actualiza una categoría existente en el sistema.
     *
     * @param category DTO con la información de la categoría actualizada.
     * @param id       Identificador único de la categoría a actualizar.
     * @return La categoría actualizada.
     * @throws CategoryNotFound Excepción lanzada si la categoría no se encuentra.
     * @throws CategoryConflict Excepción lanzada si ya existe otra categoría con el mismo nombre.
     */
    @Override
    @Cacheable
    public Category update(CategoryResponseDto category, UUID id) {
        log.info("Updating category with id: {}", id);
        Category categoryToUpdate = categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));

        categoryRepository.findByName(category.name()).ifPresent(c -> {
            if (!c.getUuid().equals(id)) {
                throw new CategoryConflict("Category already exists");
            }
        });
        onChange(Notification.Tipo.UPDATE, categoryToUpdate);
        return categoryRepository.save(categoryMapper.toCategory(category, categoryToUpdate));
    }

    /**
     * Elimina una categoría del sistema por su identificador único.
     *
     * @param id Identificador único de la categoría a eliminar.
     * @throws CategoryNotFound Excepción lanzada si la categoría no se encuentra.
     */
    @Override
    @CacheEvict
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting category with id: {}", id);
        Category categoryToUpdate = categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
        boolean hasProducts = categoryRepository.existsProductById(id);
        boolean hasSuppliers = categoryRepository.existsSupplierById(id);
        if (hasProducts) {
            log.warn("Category with id: {} has products", id);
            categoryRepository.deleteById(id);
        } else if (hasSuppliers) {
            log.warn("Category with id: {} has suppliers", id);
            categoryRepository.deleteById(id);
        } else {
            onChange(Notification.Tipo.DELETE, categoryToUpdate);
            categoryRepository.deleteById(id);
        }
    }

    /**
     * Notifica cambios en las categorías mediante WebSocket.
     *
     * @param tipo Tipo de notificación.
     * @param data Datos de la categoría afectada por el cambio.
     */
    public void onChange(Notification.Tipo tipo, Category data) {
        if (webSocketHandler == null) {
            log.warn("Not sending notification to clients because the webSocketHandler is null");
            webSocketHandler = this.webSocketConfig.webSocketProductHandler();
        }

        try {
            Notification<CategoryNotificationDto> notificacion = new Notification<>(
                    "CATEGORY",
                    tipo,
                    categoryNotificationMapper.toCategoryNotificationDto(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString(notificacion);

            log.info("Sending notification to clients: " + json);

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketHandler.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error sending message to clients", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error parsing notification to json", e);
        }
    }
}
