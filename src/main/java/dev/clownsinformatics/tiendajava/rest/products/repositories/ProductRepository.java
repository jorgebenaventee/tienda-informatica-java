package dev.clownsinformatics.tiendajava.rest.products.repositories;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByWeight(Double weight);

    List<Product> findAllByName(String name);

    List<Product> findAllByNameAndWeight(String name, Double weight);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Product p set p.isDeleted = true where p.id = ?1")
    void deleteById(UUID id);
}
