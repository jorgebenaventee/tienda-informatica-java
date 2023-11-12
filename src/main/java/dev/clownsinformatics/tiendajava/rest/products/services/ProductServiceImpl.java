package dev.clownsinformatics.tiendajava.rest.products.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadRequest;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProductsNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ProductNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"products"})
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final ProductNotificationMapper productNotificationMapper;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper, StorageService storageService, WebSocketConfig webSocketConfig, ObjectMapper mapper, ProductNotificationMapper productNotificationMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        this.mapper = mapper;
        this.productNotificationMapper = productNotificationMapper;
        webSocketHandler = webSocketConfig.webSocketProductHandler();
    }

    @Override
    public UUID getUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ProductBadRequest("Invalid UUID");
        }
    }

    @Override
    public Page<ProductResponseDto> findAll(Optional<String> name, Optional<Double> maxWeight, Optional<Double> maxPrice, Optional<Double> minStock, Optional<String> category, Pageable pageable) {
        Specification<Product> specName = (root, query, criteriaBuilder) ->
                name.map(value -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Product> specMaxWeight = (root, query, criteriaBuilder) ->
                maxWeight.map(value -> criteriaBuilder.lessThanOrEqualTo(root.get("weight"), value))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Product> specMaxPrice = (root, query, criteriaBuilder) ->
                maxPrice.map(value -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), value))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Product> spectMinStock = (root, query, criteriaBuilder) ->
                minStock.map(value -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), value))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Product> specCategory = (root, query, criteriaBuilder) ->
                category.map(value -> {
                    Join<Product, Category> categoryJoin = root.join("category");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), "%" + value.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Product> spec = Specification.where(specName).and(specMaxWeight).and(specMaxPrice).and(spectMinStock).and(specCategory);
        return productRepository.findAll(spec, pageable).map(productMapper::toProductResponseDto);
    }

    @Override
    @Cacheable
    public Product findById(String id) {
        log.info("Searching product by id: " + id);
        return productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
    }

    private Category findCategory(String categoryName) {
        var category = categoryRepository.findByNameEqualsIgnoreCase(categoryName);
        if (category.isEmpty()) {
            throw new ProductBadRequest("Category not found");
        }
        return category.get();
    }

    @Override
    @Cacheable
    public ProductResponseDto save(ProductCreateDto productCreateDto) {
        log.info("Saving product: " + productCreateDto);
        var category = findCategory(productCreateDto.category().getName());
        var productSaved = productRepository.save(productMapper.toProduct(productCreateDto, category));
        onChange(Notification.Tipo.CREATE, productSaved);
        return productMapper.toProductResponseDto(productSaved);
    }

    @Override
    @Cacheable
    @Transactional
    public ProductResponseDto update(String id, ProductUpdateDto productUpdateDto) {
        log.info("Updating product with id: " + id);
        var actualProduct = productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
        Category category = null;

        if (productUpdateDto.category() != null && productUpdateDto.category().getName().isEmpty()) {
            category = findCategory(productUpdateDto.category().getName());
        } else {
            category = actualProduct.getCategory();
        }

        var productUpdated = productRepository.save(productMapper.toProduct(productUpdateDto, actualProduct, category));
        onChange(Notification.Tipo.UPDATE, productUpdated);
        return productMapper.toProductResponseDto(productUpdated);
    }

    @Override
    @CachePut(key = "#id")
    @Transactional
    public Product updateImage(String id, MultipartFile file) {
        if (!file.isEmpty()) {
            String image = storageService.store(file);
            String urlImage = storageService.getUrl(image);

            Product product = findById(id);
            storageService.delete(product.getImg());
            product.setImg(urlImage);
            onChange(Notification.Tipo.UPDATE, product);
            return productRepository.save(product);
        } else {
            throw new ProductBadRequest("Image is empty");
        }
    }

    @Override
    @CacheEvict
    @Transactional
    public void deleteById(String id) {
        log.info("Deleting product with id: " + id);
        var product = productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
        productRepository.deleteById(getUUID(id));
        if (product.getImg() != null && !product.getImg().equals(Product.IMAGE_DEFAULT)) {
            storageService.delete(product.getImg());
        }
        onChange(Notification.Tipo.DELETE, product);
    }

    public void onChange(Notification.Tipo tipo, Product data) {
        if (webSocketHandler == null) {
            log.warn("Not sending notification to clients because the webSocketHandler is null");
            webSocketHandler = this.webSocketConfig.webSocketProductHandler();
        }

        try {
            Notification<ProductsNotificationDto> notificacion = new Notification<>(
                    "PRODUCTS",
                    tipo,
                    productNotificationMapper.toProductNotificationDto(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));

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
