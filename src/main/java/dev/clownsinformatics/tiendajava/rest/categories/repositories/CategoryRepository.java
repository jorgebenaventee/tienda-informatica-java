package dev.clownsinformatics.tiendajava.rest.categories.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByNameEqualsIgnoreCase(String name);

    Optional<Category> findByUuid(UUID uuid);

    Optional<Category> findByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.category.uuid = :id")
    Boolean existsProductById(UUID id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Supplier p WHERE p.category.uuid = :id")
    Boolean existsSupplierById(UUID id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Category c set c.isDeleted = true where c.uuid = ?1")
    void deleteById(UUID id);
}