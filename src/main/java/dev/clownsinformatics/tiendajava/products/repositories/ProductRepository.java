package dev.clownsinformatics.tiendajava.products.repositories;

import dev.clownsinformatics.tiendajava.products.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findAllByWeight(Double weight);

    List<Product> findAllByName(String name);

    List<Product> findAllByNameAndWeight(String name, Double weight);

    Optional<Product> findById(UUID id);

    Optional<Product> findByIdCategory(UUID idCategory);

    Product save(Product product);

    void deleteById(UUID id);

    void deleteByIdCategory(UUID idCategory);

    void deleteAll();
}
