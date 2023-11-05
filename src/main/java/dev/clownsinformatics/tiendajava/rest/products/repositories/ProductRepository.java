package dev.clownsinformatics.tiendajava.rest.products.repositories;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByWeight(Double weight);

    List<Product> findAllByName(String name);

    List<Product> findAllByNameAndWeight(String name, Double weight);
}