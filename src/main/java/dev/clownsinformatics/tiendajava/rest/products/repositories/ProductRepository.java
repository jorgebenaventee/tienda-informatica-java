package dev.clownsinformatics.tiendajava.rest.products.repositories;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
}
