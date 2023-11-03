package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ProveedorRepositoryImplTest {

    private final Proveedor proveedor1 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .nombre("Proveedor 1")
            .contacto(1)
            .direccion("Calle 1")
            .fechaContratacion(LocalDate.now())
            .build();

    private final Proveedor proveedor2 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .nombre("Proveedor 2")
            .contacto(2)
            .direccion("Calle 2")
            .fechaContratacion(LocalDate.now())
            .build();

    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        proveedorRepository.deleteAll();
        //entityManager.merge(categoria);
        //entityManager.flush();

        entityManager.merge(proveedor1);
        entityManager.merge(proveedor2);
        entityManager.flush();
    }

    @Test
    void getByUUID() {
        proveedorRepository.getByUUID(proveedor1.getIdProveedor());
        assertAll(
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor1.getIdProveedor())),
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor2.getIdProveedor()))
        );
    }

    @Test
    void getByNombreContainingIgnoreCase() {
        proveedorRepository.getByNombreContainingIgnoreCase("Proveedor 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByNombreContainingIgnoreCase("Proveedor 1")),
                () -> assertNotNull(proveedorRepository.getByNombreContainingIgnoreCase("Proveedor 2"))
        );
    }

    @Test
    void getByDireccionContainingIgnoreCase() {
        proveedorRepository.getByDireccionContainingIgnoreCase("Calle 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByDireccionContainingIgnoreCase("Calle 1")),
                () -> assertNotNull(proveedorRepository.getByDireccionContainingIgnoreCase("Calle 2"))
        );
    }

    @Test
    void getByNombreAndDireccionContainingIgnoreCase() {
        proveedorRepository.getByNombreAndDireccionContainingIgnoreCase("Proveedor 1", "Calle 1");
        assertAll(
                () -> assertNotNull(proveedorRepository.getByNombreAndDireccionContainingIgnoreCase("Proveedor 1", "Calle 1")),
                () -> assertNotNull(proveedorRepository.getByNombreAndDireccionContainingIgnoreCase("Proveedor 2", "Calle 2"))
        );
    }

    @Test
    void deleteByUUID() {
        proveedorRepository.deleteByUUID(proveedor1.getIdProveedor());
        assertAll(
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor1.getIdProveedor())),
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor2.getIdProveedor()))
        );
    }

    @Test
    void generateUUID() {
        proveedorRepository.generateUUID();
        assertAll(
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor1.getIdProveedor())),
                () -> assertNotNull(proveedorRepository.getByUUID(proveedor2.getIdProveedor()))
        );
    }
}