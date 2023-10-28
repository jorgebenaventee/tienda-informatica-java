package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.exceptions.ProductBadUUID;
import dev.clownsinformatics.tiendajava.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.products.models.Categories;
import dev.clownsinformatics.tiendajava.products.models.Product;
import dev.clownsinformatics.tiendajava.products.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"products"})
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<Product> findAll(Categories category, String name) {
        if ((category == null) && (name == null || name.isEmpty())) {
            log.info("Buscando todos los productos");
            return productRepository.findAll();
        }
        if ((name != null && !name.isEmpty()) && category == null) {
            log.info("Buscando productos por nombre: " + name);
            return productRepository.findAllByName(name);
        }
        if (name == null || name.isEmpty()) {
            log.info("Buscando productos por categoria: " + category);
            return productRepository.findAllByCategory(category);
        }
        log.info("Buscando productos por categoria: " + category + " y nombre: " + name);
        return productRepository.findAllByNameAndCategory(name, category);
    }

    @Override
    @CachePut(key = "#id")
    public Product findById(Long id) {
        log.info("Buscando producto por id: " + id);
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFound(id.toString()));
    }

    @Override
    public Product findByIdCategory(Long idCategory) {
        log.info("Buscando producto por id de categoria: " + idCategory);
        return productRepository.findByIdCategory(idCategory).orElseThrow(() -> new ProductNotFound(idCategory.toString()));
    }

    @Override
    public Product findByUUID(String uuid) {
        log.info("Buscando producto por uuid: " + uuid);
        try {
            return productRepository.findByUUID(UUID.fromString(uuid)).orElseThrow(() -> new ProductNotFound(uuid));
        } catch (IllegalArgumentException e) {
            throw new ProductBadUUID(uuid);
        }
    }

    @Override
    @CachePut(key = "#result.id")
    public Product save(ProductCreateDto productCreateDto) {
        log.info("Creando producto: " + productCreateDto);
        Long id = productRepository.nextId();
        Product product = productMapper.toProduct(id, productCreateDto);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, ProductUpdateDto productUpdateDto) {
        log.info("Actualizando producto con id: " + id);
        Product product = findById(id);
        Product updatedProduct = productMapper.toProduct(productUpdateDto, product);
        return productRepository.save(updatedProduct);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Eliminando producto con id: " + id);
        findById(id);
        productRepository.deleteById(id);
    }
}
