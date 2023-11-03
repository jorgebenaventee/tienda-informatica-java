package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll(Double weight, String name);

    Product findById(String id);

    Product save(ProductCreateDto productCreateDto);

    Product update(String id, ProductUpdateDto productUpdateDto);

    void deleteById(String id);
}
