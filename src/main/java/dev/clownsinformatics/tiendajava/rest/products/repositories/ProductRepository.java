package dev.clownsinformatics.tiendajava.rest.products.repositories;

import dev.clownsinformatics.tiendajava.rest.products.models.Categories;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findAllByCategory(Categories category);

    List<Product> findAllByName(String name);

    List<Product> findAllByNameAndCategory(String name, Categories category);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdCategory(Long idCategory);

    Optional<Product> findByUUID(UUID uuid);

    Product save(Product product);

    void deleteById(Long id);

    void deleteByIdCategory(Long idCategory);

    void deleteByUUID(UUID uuid);

    void deleteAll();

    Long nextId();
}
