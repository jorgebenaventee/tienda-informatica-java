package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.products.models.Categories;
import dev.clownsinformatics.tiendajava.products.models.Product;
import dev.clownsinformatics.tiendajava.products.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
        if (category == null && (name != null && !name.isEmpty())) {
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
    public Product findById(Long id) {
        return null;
    }

    @Override
    public Product findByIdCategory(Long idCategory) {
        return null;
    }

    @Override
    public Product findByUUID(UUID uuid) {
        return null;
    }

    @Override
    public Product save(ProductCreateDto productCreateDto) {
        return null;
    }

    @Override
    public Product update(Long id, ProductUpdateDto productUpdateDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
