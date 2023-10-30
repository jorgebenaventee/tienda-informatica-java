package dev.clownsinformatics.tiendajava.proveedores.mapper;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProveedorMapperTest {

    private final Proveedor proveedor = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .nombre("Proveedor 1")
            .contacto(1)
            .direccion("Calle 1")
            .fechaContratacion(LocalDate.now())
            .build();

    private final ProveedorMapper proveedorMapper = new ProveedorMapper();

    @Test
    void toProveedor() {
        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto(
                "Proveedor Creado",
                2,
                "Calle Creada"
        );

        assertAll(
                () -> assertEquals(proveedor.getIdProveedor(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getIdProveedor()),
                () -> assertEquals(proveedorCreateDto.nombre(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getNombre()),
                () -> assertEquals(proveedorCreateDto.contacto(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getContacto()),
                () -> assertEquals(proveedorCreateDto.direccion(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getDireccion())
        );
    }

    @Test
    void testToProveedor() {
        UUID uuid = UUID.randomUUID();
        ProveedorUpdateDto proveedorUpdateDto = new ProveedorUpdateDto(
                "Proveedor 1",
                "1",
                "Calle 1"
        );

        Proveedor proveedor1 = Proveedor.builder()
                .idProveedor(uuid)
                .nombre("Proveedor 1")
                .contacto(1)
                .direccion("Calle 1")
                .fechaContratacion(LocalDate.now())
                .build();

        assertAll(
                () -> assertEquals(proveedor1.getIdProveedor(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getIdProveedor()),
                () -> assertEquals(proveedor1.getNombre(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getNombre()),
                () -> assertEquals(proveedor1.getContacto(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getContacto()),
                () -> assertEquals(proveedor1.getDireccion(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getDireccion())
        );
    }

    @Test
    void toProveedorDto() {
        Proveedor proveedor1 = Proveedor.builder()
                .idProveedor(UUID.randomUUID())
                .nombre("Proveedor 1")
                .contacto(1)
                .direccion("Calle 1")
                .fechaContratacion(LocalDate.now())
                .build();

        assertAll(
                () -> assertEquals(proveedor1.getNombre(), proveedorMapper.toProveedorDto(proveedor1).nombre()),
                () -> assertEquals(proveedor1.getContacto(), proveedorMapper.toProveedorDto(proveedor1).contacto()),
                () -> assertEquals(proveedor1.getDireccion(), proveedorMapper.toProveedorDto(proveedor1).direccion())
        );
    }
}