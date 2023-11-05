package dev.clownsinformatics.tiendajava.rest.products.services;

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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"products"})
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final StorageService storageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper, StorageService storageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.storageService = storageService;
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
    public List<Product> findAll(Double weight, String name) {
        if ((weight == null) && (name == null || name.isEmpty())) {
            log.info("Searching all products");
            return productRepository.findAll();
        }
        if ((name != null && !name.isEmpty()) && weight == null) {
            log.info("Searching products by name: " + name);
            return productRepository.findAllByName(name);
        }
        if (name == null || name.isEmpty()) {
            log.info("Searching products by category: " + weight);
            return productRepository.findAllByWeight(weight);
        }
        log.info("Searching products by name: " + name + " and category: " + weight);
        return productRepository.findAllByNameAndWeight(name, weight);
    }

    @Override
    @Cacheable
    public Product findById(String id) {
        log.info("Searching product by id: " + id);
        return productRepository.findById(getUUID(id)).orElseThrow(() -> new ProductNotFound(id));
    }

    private Category findCategory(String categoryName) {
        var category = categoryRepository.findByNameContainingIgnoreCase(categoryName);
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

    }
}
