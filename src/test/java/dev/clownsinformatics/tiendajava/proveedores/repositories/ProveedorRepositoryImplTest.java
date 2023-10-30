package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProveedorRepositoryImplTest {

    ProveedorRepositoryImpl proveedorRepository = new ProveedorRepositoryImpl();

    Proveedor proveedor1 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .nombre("Proveedor 1")
            .contacto(1)
            .direccion("Calle 1")
            .fechaContratacion(LocalDate.now())
            .build();

    Proveedor proveedor2 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .nombre("Proveedor 2")
            .contacto(2)
            .direccion("Calle 2")
            .fechaContratacion(LocalDate.now())
            .build();

    @BeforeEach
    void setUp() {
        proveedorRepository.proveedores.clear();
    }

    @Test
    void getAll() {
        proveedorRepository.proveedores.put(proveedor1.getIdProveedor(), proveedor1);
        proveedorRepository.proveedores.put(proveedor2.getIdProveedor(), proveedor2);
        assertAll(
                () -> assertEquals(2, proveedorRepository.getAll().size()),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor2))
        );
    }

    @Test
    void getByUUID() {
        proveedorRepository.proveedores.put(proveedor1.getIdProveedor(), proveedor1);
        proveedorRepository.proveedores.put(proveedor2.getIdProveedor(), proveedor2);
        assertAll(
                () -> assertEquals(proveedor1, proveedorRepository.getByUUID(proveedor1.getIdProveedor()).get()),
                () -> assertEquals(proveedor2, proveedorRepository.getByUUID(proveedor2.getIdProveedor()).get())
        );
    }

    @Test
    void getByNombre() {
        proveedorRepository.proveedores.put(proveedor1.getIdProveedor(), proveedor1);
        proveedorRepository.proveedores.put(proveedor2.getIdProveedor(), proveedor2);
        assertAll(
                () -> assertEquals(1, proveedorRepository.getByNombre(proveedor1.getNombre()).size()),
                () -> assertEquals(1, proveedorRepository.getByNombre(proveedor2.getNombre()).size()),
                () -> assertTrue(proveedorRepository.getByNombre(proveedor1.getNombre()).contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getByNombre(proveedor2.getNombre()).contains(proveedor2))
        );
    }

    @Test
    void getByDireccion() {
        proveedorRepository.proveedores.put(proveedor1.getIdProveedor(), proveedor1);
        proveedorRepository.proveedores.put(proveedor2.getIdProveedor(), proveedor2);
        assertAll(
                () -> assertEquals(1, proveedorRepository.getByDireccion(proveedor1.getDireccion()).size()),
                () -> assertEquals(1, proveedorRepository.getByDireccion(proveedor2.getDireccion()).size()),
                () -> assertTrue(proveedorRepository.getByDireccion(proveedor1.getDireccion()).contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getByDireccion(proveedor2.getDireccion()).contains(proveedor2))
        );
    }

    @Test
    void getByNombreAndDireccion() {
        proveedorRepository.proveedores.put(proveedor1.getIdProveedor(), proveedor1);
        proveedorRepository.proveedores.put(proveedor2.getIdProveedor(), proveedor2);
        assertAll(
                () -> assertEquals(1, proveedorRepository.getByNombreAndDireccion(proveedor1.getNombre(), proveedor1.getDireccion()).size()),
                () -> assertEquals(1, proveedorRepository.getByNombreAndDireccion(proveedor2.getNombre(), proveedor2.getDireccion()).size()),
                () -> assertTrue(proveedorRepository.getByNombreAndDireccion(proveedor1.getNombre(), proveedor1.getDireccion()).contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getByNombreAndDireccion(proveedor2.getNombre(), proveedor2.getDireccion()).contains(proveedor2))
        );
    }

    @Test
    void save() {
        proveedorRepository.save(proveedor1);
        proveedorRepository.save(proveedor2);
        assertAll(
                () -> assertEquals(2, proveedorRepository.getAll().size()),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor2))
        );
    }

    @Test
    void update() {
        proveedorRepository.save(proveedor1);
        proveedorRepository.save(proveedor2);
        Proveedor proveedor3 = Proveedor.builder()
                .idProveedor(proveedor1.getIdProveedor())
                .nombre("Proveedor 3")
                .contacto(3)
                .direccion("Calle 3")
                .build();
        proveedorRepository.update(proveedor3);
        assertAll(
                () -> assertEquals(2, proveedorRepository.getAll().size()),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor3)),
                () -> assertFalse(proveedorRepository.getAll().contains(proveedor1))
        );
    }

    @Test
    void deleteByUUID() {
        proveedorRepository.save(proveedor1);
        proveedorRepository.save(proveedor2);
        proveedorRepository.deleteByUUID(proveedor1.getIdProveedor());
        assertAll(
                () -> assertEquals(1, proveedorRepository.getAll().size()),
                () -> assertFalse(proveedorRepository.getAll().contains(proveedor1)),
                () -> assertTrue(proveedorRepository.getAll().contains(proveedor2))
        );
    }

    @Test
    void generateUUID() {
        UUID uuid = proveedorRepository.generateUUID();
        assertNotNull(uuid);
    }
}