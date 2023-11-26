package dev.clownsinformatics.tiendajava.rest.products.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadRequest;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import dev.clownsinformatics.tiendajava.rest.suppliers.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.suppliers.services.SupplierService;
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
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductMapper productMapper;
    private final SupplierMapper supplierMapper;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final ProductNotificationMapper productNotificationMapper;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ProductMapper productMapper, StorageService storageService, WebSocketConfig webSocketConfig, ObjectMapper mapper, ProductNotificationMapper productNotificationMapper, SupplierService supplierService, SupplierMapper supplierMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        this.mapper = mapper;
        this.productNotificationMapper = productNotificationMapper;
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
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
    public Page<ProductResponseDto> findAll(Optional<String> name, Optional<Double> maxWeight, Optional<Double> maxPrice, Optional<Double> minStock, Optional<String> category,Optional<Boolean> isDeleted, Pageable pageable) {
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

        Specification<Product> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(value -> criteriaBuilder.equal(root.get("isDeleted"), value))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(false)));

        Specification<Product> spec = Specification.where(specName).and(specMaxWeight).and(specMaxPrice).and(spectMinStock).and(specCategory).and(specIsDeleted);
        return productRepository.findAll(spec, pageable).map(productMapper::toProductResponseDto);
    }

    @Override
    @Cacheable
    public ProductResponseDto findById(String id) {
        log.info("Searching product by id: " + id);
        Product product = productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
        return productMapper.toProductResponseDto(product);
    }

    @Override
    @Cacheable
    public ProductResponseDto save(ProductCreateDto productCreateDto) {
        log.info("Saving product: " + productCreateDto);
        log.info("Category: " + productCreateDto.category());
        Category category = categoryService.findByName(productCreateDto.category().getName());
        Supplier supplier = supplierMapper.toSupplier(supplierService.findByName(productCreateDto.supplier().getName()));
        Product productSaved = productRepository.save(productMapper.toProduct(productCreateDto, category, supplier));
        onChange(Notification.Tipo.CREATE, productSaved);
        return productMapper.toProductResponseDto(productSaved);
    }

    @Override
    @Cacheable
    @Transactional
    public ProductResponseDto update(String id, ProductUpdateDto productUpdateDto) {
        log.info("Updating product with id: " + id);
        var actualProduct = productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
        Category category;

        if (productUpdateDto.category() != null && productUpdateDto.category().getName().isEmpty()) {
            category = categoryService.findById(productUpdateDto.category().getUuid());
        } else {
            category = actualProduct.getCategory();
        }
        Supplier supplier = supplierMapper.toSupplier(supplierService.findByName(productUpdateDto.supplier().getName()));
        Product productUpdated = productRepository.save(productMapper.toProduct(productUpdateDto, actualProduct, category, supplier));
        onChange(Notification.Tipo.UPDATE, productUpdated);
        return productMapper.toProductResponseDto(productUpdated);
    }

    @Override
    @CachePut(key = "#id")
    @Transactional
    public ProductResponseDto updateImage(String id, MultipartFile file) {
        if (!file.isEmpty()) {
            String image = storageService.store(file);
            String urlImage = storageService.getUrl(image);

            Product product = productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
            storageService.delete(product.getImg());
            product.setImg(urlImage);
            onChange(Notification.Tipo.UPDATE, product);
            Product productSaved = productRepository.save(product);
            return productMapper.toProductResponseDto(productSaved);
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
