package dev.clownsinformatics.tiendajava.rest.proveedores.mapper;

import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SupplierMapper {
    public Supplier toProveedor(SupplierCreateDto proveedorCreateDto) {
        return Supplier.builder()

                .name(proveedorCreateDto.name())
                .contact(proveedorCreateDto.contact())
                .address(proveedorCreateDto.address())
                .category(proveedorCreateDto.category())
                .build();
    }

    public Supplier toProveedor(SupplierUpdateDto proveedorUpdateDto, Supplier proveedor) {
        return Supplier.builder()
                .id(proveedor.getId())
                .name(proveedorUpdateDto.name() != null ? proveedorUpdateDto.name() : proveedor.getName())
                .contact(proveedorUpdateDto.contact() != null ? Integer.valueOf(proveedorUpdateDto.contact()) : proveedor.getContact())
                .address(proveedorUpdateDto.address() != null ? proveedorUpdateDto.address() : proveedor.getAddress())
                .category(proveedorUpdateDto.category() != null ? proveedorUpdateDto.category() : proveedor.getCategory())
                .build();
    }


    public SupplierResponseDto toProveedorDto(Supplier proveedor) {
        return new SupplierResponseDto(
                proveedor.getId(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getAddress(),
                proveedor.getDateOfHire(),
                proveedor.getCategory()
        );
    }
}