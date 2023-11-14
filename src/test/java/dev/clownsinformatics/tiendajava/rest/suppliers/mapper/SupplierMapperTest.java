package dev.clownsinformatics.tiendajava.rest.suppliers.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
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

    private final Supplier supplier = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category)
            .build();

    private final SupplierMapper supplierMapper = new SupplierMapper();

    @Test
    void toSupplier() {
        SupplierCreateDto supplierCreateDto = new SupplierCreateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category
        );

        assertAll(
                () -> assertEquals(supplierCreateDto.name(), supplierMapper.toSupplier(supplierCreateDto).getName()),
                () -> assertEquals(supplierCreateDto.contact(), supplierMapper.toSupplier(supplierCreateDto).getContact()),
                () -> assertEquals(supplierCreateDto.address(), supplierMapper.toSupplier(supplierCreateDto).getAddress())
        );
    }

    @Test
    void testToSupplier() {
        UUID uuid = UUID.randomUUID();
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category

        );

        Supplier supplier1 = Supplier.builder()
                .id(uuid)
                .name("Supplier 1")
                .contact(1)
                .address("Calle 1")
                .dateOfHire(LocalDateTime.now())
                .category(category)
                .build();

        assertAll(
                () -> assertEquals(supplier1.getId(), supplierMapper.toSupplier(supplierUpdateDto, supplier1).getId()),
                () -> assertEquals(supplier1.getName(), supplierMapper.toSupplier(supplierUpdateDto, supplier1).getName()),
                () -> assertEquals(supplier1.getContact(), supplierMapper.toSupplier(supplierUpdateDto, supplier1).getContact()),
                () -> assertEquals(supplier1.getAddress(), supplierMapper.toSupplier(supplierUpdateDto, supplier1).getAddress())
        );
    }

    @Test
    void toSupplierDto() {
        Supplier supplier1 = Supplier.builder()
                .id(UUID.randomUUID())
                .name("Supplier 1")
                .contact(1)
                .address("Calle 1")
                .dateOfHire(LocalDateTime.now())
                .category(category)
                .build();

        assertAll(
                () -> assertEquals(supplier1.getName(), supplierMapper.toSupplierDto(supplier1).name()),
                () -> assertEquals(supplier1.getContact(), supplierMapper.toSupplierDto(supplier1).contact()),
                () -> assertEquals(supplier1.getAddress(), supplierMapper.toSupplierDto(supplier1).address())
        );
    }
}