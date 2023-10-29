package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.products.models.Product;
import dev.clownsinformatics.tiendajava.products.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"products"})
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<Product> findAll(Double weight, String name) {
        if ((weight == null) && (name == null || name.isEmpty())) {
            log.info("Buscando todos los productos");
            return productRepository.findAll();
        }
        if ((name != null && !name.isEmpty()) && weight == null) {
            log.info("Buscando productos por nombre: " + name);
            return productRepository.findAllByName(name);
        }
        if (name == null || name.isEmpty()) {
            log.info("Buscando productos por categoria: " + weight);
            return productRepository.findAllByWeight(weight);
        }
        log.info("Buscando productos por categoria: " + weight + " y nombre: " + name);
        return productRepository.findAllByNameAndWeight(name, weight);
    }

    @Override
    @Cacheable
    public Product findById(String id) {
        log.info("Buscando producto por id: " + id);
        return productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ProductNotFound(id));
    }

    @Override
    @Cacheable
    public Product findByIdCategory(String idCategory) {
        log.info("Buscando producto por id de categoria: " + idCategory);
        return productRepository.findByIdCategory(UUID.fromString(idCategory)).orElseThrow(() -> new ProductNotFound(idCategory));
    }

    @Override
    @CachePut
    public Product save(ProductCreateDto productCreateDto) {
        log.info("Creando producto: " + productCreateDto);
        Product product = productMapper.toProduct(productRepository.getRandomUUID(), productCreateDto);
        return productRepository.save(product);
    }

    @Override
    @CachePut
    public Product update(String id, ProductUpdateDto productUpdateDto) {
        log.info("Actualizando producto con id: " + id);
        Product product = findById(id);
        Product updatedProduct = productMapper.toProduct(productUpdateDto, product);
        return productRepository.save(updatedProduct);
    }

    @Override
    @CacheEvict
    public void deleteById(String id) {
        log.info("Eliminando producto con id: " + id);
        findById(id);
        productRepository.deleteById(UUID.fromString(id));
    }
}
