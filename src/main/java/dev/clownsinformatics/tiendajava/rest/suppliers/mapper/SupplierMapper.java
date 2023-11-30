package dev.clownsinformatics.tiendajava.rest.suppliers.mapper;

import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public Supplier toSupplier(SupplierCreateDto supplierCreateDto) {
        return Supplier.builder()
                .name(supplierCreateDto.name())
                .contact(supplierCreateDto.contact())
                .address(supplierCreateDto.address())
                .category(supplierCreateDto.category())
                .isDeleted(supplierCreateDto.isDeleted() != null ? supplierCreateDto.isDeleted() : Boolean.FALSE)
                .build();
    }

    public Supplier toSupplier(SupplierUpdateDto supplierUpdateDto, Supplier supplier) {
        return Supplier.builder()
                .id(supplier.getId())
                .name(supplierUpdateDto.name() != null ? supplierUpdateDto.name() : supplier.getName())
                .contact(supplierUpdateDto.contact() != null ? supplierUpdateDto.contact() : supplier.getContact())
                .address(supplierUpdateDto.address() != null ? supplierUpdateDto.address() : supplier.getAddress())
                .category(supplierUpdateDto.category() != null ? supplierUpdateDto.category() : supplier.getCategory())
                .isDeleted(supplierUpdateDto.isDeleted() != null ? supplierUpdateDto.isDeleted() : supplier.getIsDeleted())
                .build();
    }

    public Supplier toSupplier(SupplierResponseDto supplierResponseDto) {
        return Supplier.builder()
                .id(supplierResponseDto.id())
                .name(supplierResponseDto.name())
                .contact(supplierResponseDto.contact())
                .address(supplierResponseDto.address())
                .category(supplierResponseDto.category())
                .build();
    }

    public SupplierResponseDto toSupplierDto(Supplier supplier) {
        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getContact(),
                supplier.getAddress(),
                supplier.getDateOfHire(),
                supplier.getCategory(),
                supplier.getIsDeleted()
        );
    }


}