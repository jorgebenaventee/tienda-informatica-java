package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ProveedorRepositoryImplTest {

    private final Category category = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Proveedor proveedor1 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final Proveedor proveedor2 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .name("Proveedor 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    @Autowired
    private ProveedorRepository proveedorRepository;
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
    void getByIdProveedor() {
        proveedorRepository.getByIdProveedor(proveedor1.getIdProveedor());
        assertAll(
                () -> assertNotNull(proveedorRepository.getByIdProveedor(proveedor1.getIdProveedor())),
                () -> assertNotNull(proveedorRepository.getByIdProveedor(proveedor2.getIdProveedor()))
        );
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

    @Test
    void deleteByIdProveedor() {
        proveedorRepository.deleteByIdProveedor(proveedor1.getIdProveedor());
        assertAll(
                () -> assertNotNull(proveedorRepository.getByIdProveedor(proveedor1.getIdProveedor())),
                () -> assertNotNull(proveedorRepository.getByIdProveedor(proveedor2.getIdProveedor()))
        );
    }
}