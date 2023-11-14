package dev.clownsinformatics.tiendajava.proveedores.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplierMapperTest {

    private final Category category = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Supplier proveedor = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final SupplierMapper proveedorMapper = new SupplierMapper();

    @Test
    void toProveedor() {
        SupplierCreateDto proveedorCreateDto = new SupplierCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category
        );

        assertAll(
                () -> assertEquals(proveedorCreateDto.name(), proveedorMapper.toProveedor(proveedorCreateDto).getName()),
                () -> assertEquals(proveedorCreateDto.contact(), proveedorMapper.toProveedor(proveedorCreateDto).getContact()),
                () -> assertEquals(proveedorCreateDto.address(), proveedorMapper.toProveedor(proveedorCreateDto).getAddress())
        );
    }

    @Test
    void testToProveedor() {
        UUID uuid = UUID.randomUUID();
        SupplierUpdateDto proveedorUpdateDto = new SupplierUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category

        );

        Supplier proveedor1 = Supplier.builder()
                .id(uuid)
                .name("Proveedor 1")
                .contact(1)
                .address("Calle 1")
                .dateOfHire(LocalDateTime.now())
                .category(category)
                .build();

        assertAll(
                () -> assertEquals(proveedor1.getId(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getId()),
                () -> assertEquals(proveedor1.getName(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getName()),
                () -> assertEquals(proveedor1.getContact(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getContact()),
                () -> assertEquals(proveedor1.getAddress(), proveedorMapper.toProveedor(proveedorUpdateDto, proveedor1).getAddress())
        );
    }

    @Test
    void toProveedorDto() {
        Supplier proveedor1 = Supplier.builder()
                .id(UUID.randomUUID())
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