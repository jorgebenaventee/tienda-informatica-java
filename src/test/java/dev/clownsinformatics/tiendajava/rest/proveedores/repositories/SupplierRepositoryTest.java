package dev.clownsinformatics.tiendajava.rest.proveedores.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
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

    private final Supplier proveedor1 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final Supplier proveedor2 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Proveedor 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    @Autowired
    private SupplierRepository proveedorRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        proveedorRepository.deleteAll();
        entityManager.merge(category);
        entityManager.flush();

        entityManager.merge(proveedor1);
        entityManager.merge(proveedor2);
        entityManager.flush();
    }


    @Test
    void getByNameContainingIgnoreCase() {
        proveedorRepository.getByNameContainingIgnoreCase("Proveedor 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByNameContainingIgnoreCase("Proveedor 1")),
                () -> assertNotNull(proveedorRepository.getByNameContainingIgnoreCase("Proveedor 2"))
        );
    }

    @Test
    void getByAddressContainingIgnoreCase() {
        proveedorRepository.getByAddressContainingIgnoreCase("Calle 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByAddressContainingIgnoreCase("Calle 1")),
                () -> assertNotNull(proveedorRepository.getByAddressContainingIgnoreCase("Calle 2"))
        );
    }

    @Test
    void getByNameAndAddressContainingIgnoreCase() {
        proveedorRepository.getByNameAndAddressContainingIgnoreCase("Proveedor 1", "Calle 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByNameAndAddressContainingIgnoreCase("Proveedor 1", "Calle 1")),
                () -> assertNotNull(proveedorRepository.getByNameAndAddressContainingIgnoreCase("Proveedor 2", "Calle 2"))
        );
    }

}