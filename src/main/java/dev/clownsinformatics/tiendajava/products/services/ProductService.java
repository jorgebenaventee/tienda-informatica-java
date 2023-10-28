package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.models.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll(String category, String name);

    Product findById(Long id);

    Product findByIdCategory(Long idCategory);

    Product findByUUID(UUID uuid);

    Product save(Product product);

    Product update(Product product);

    void deleteById(Long id);
}
