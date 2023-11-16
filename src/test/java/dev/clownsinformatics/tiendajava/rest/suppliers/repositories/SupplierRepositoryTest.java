package dev.clownsinformatics.tiendajava.rest.suppliers.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SupplierRepositoryTest {

    private final Category category = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Supplier supplier1 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final Supplier supplier2 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        supplierRepository.deleteAll();
        entityManager.merge(category);
        entityManager.flush();

        entityManager.merge(supplier1);
        entityManager.merge(supplier2);
        entityManager.flush();
    }


    @Test
    void getByNameContainingIgnoreCase() {
        supplierRepository.getByNameContainingIgnoreCase("Supplier 1");
        assertAll(
                () -> assertNotNull(supplierRepository.getByNameContainingIgnoreCase("Supplier 1")),
                () -> assertNotNull(supplierRepository.getByNameContainingIgnoreCase("Supplier 2"))
        );
    }

    @Test
    void getByAddressContainingIgnoreCase() {
        supplierRepository.getByAddressContainingIgnoreCase("Calle 1");
        assertAll(
                () -> assertNotNull(supplierRepository.getByAddressContainingIgnoreCase("Calle 1")),
                () -> assertNotNull(supplierRepository.getByAddressContainingIgnoreCase("Calle 2"))
        );
    }

    @Test
    void getByNameAndAddressContainingIgnoreCase() {
        supplierRepository.getByNameAndAddressContainingIgnoreCase("Supplier 1", "Calle 1");
        assertAll(
                () -> assertNotNull(supplierRepository.getByNameAndAddressContainingIgnoreCase("Supplier 1", "Calle 1")),
                () -> assertNotNull(supplierRepository.getByNameAndAddressContainingIgnoreCase("Supplier 2", "Calle 2"))
        );
    }

}