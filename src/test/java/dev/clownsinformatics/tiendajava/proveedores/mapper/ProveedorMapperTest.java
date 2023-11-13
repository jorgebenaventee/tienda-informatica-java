package dev.clownsinformatics.tiendajava.proveedores.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProveedorMapperTest {

    private final Category category = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Proveedor proveedor = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final ProveedorMapper proveedorMapper = new ProveedorMapper();

    @Test
    void toProveedor() {
        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category
        );

        assertAll(
                () -> assertEquals(proveedor.getIdProveedor(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getIdProveedor()),
                () -> assertEquals(proveedorCreateDto.name(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getName()),
                () -> assertEquals(proveedorCreateDto.contact(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getContact()),
                () -> assertEquals(proveedorCreateDto.address(), proveedorMapper.toProveedor(proveedorCreateDto, proveedor.getIdProveedor()).getAddress())
        );
    }

    @Test
    void testToProveedor() {
        UUID uuid = UUID.randomUUID();
        ProveedorUpdateDto proveedorUpdateDto = new ProveedorUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category

        );

        Proveedor proveedor1 = Proveedor.builder()
                .idProveedor(uuid)
                .name("Proveedor 1")
                .contact(1)
                .address("Calle 1")
                .dateOfHire(LocalDateTime.now())
                .category(category)
                .build();

        assertAll(
                () -> assertEquals(proveedor1.getIdProveedor(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getIdProveedor()),
                () -> assertEquals(proveedor1.getName(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getName()),
                () -> assertEquals(proveedor1.getContact(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getContact()),
                () -> assertEquals(proveedor1.getAddress(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getAddress())
        );
    }

    @Test
    void toProveedorDto() {
        Proveedor proveedor1 = Proveedor.builder()
                .idProveedor(UUID.randomUUID())
                .name("Proveedor 1")
                .contact(1)
                .address("Calle 1")
                .dateOfHire(LocalDateTime.now())
                .category(category)
                .build();

        assertAll(
                () -> assertEquals(proveedor1.getName(), proveedorMapper.toProveedorDto(proveedor1).name()),
                () -> assertEquals(proveedor1.getContact(), proveedorMapper.toProveedorDto(proveedor1).contact()),
                () -> assertEquals(proveedor1.getAddress(), proveedorMapper.toProveedorDto(proveedor1).address())
        );
    }
}