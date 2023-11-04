package dev.clownsinformatics.tiendajava.proveedores.mapper;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorResponseDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.proveedores.repositories.ProveedorRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProveedorMapper {
    public Proveedor toProveedor(ProveedorCreateDto proveedorCreateDto, UUID idProveedor) {
        return Proveedor.builder()
                .idProveedor(idProveedor)
                .name(proveedorCreateDto.name())
                .contact(proveedorCreateDto.contact())
                .address(proveedorCreateDto.address())
                .category(proveedorCreateDto.category())
                .build();
    }

    public Proveedor toProveedor(ProveedorUpdateDto proveedorUpdateDto, Proveedor proveedor) {
        return Proveedor.builder()
                .idProveedor(proveedor.getIdProveedor())
                .name(proveedorUpdateDto.name() != null ? proveedorUpdateDto.name() : proveedor.getName())
                .contact(proveedorUpdateDto.contact() != null ? Integer.valueOf(proveedorUpdateDto.contact()) : proveedor.getContact())
                .address(proveedorUpdateDto.address() != null ? proveedorUpdateDto.address() : proveedor.getAddress())
                .category(proveedorUpdateDto.category() != null ? proveedorUpdateDto.category() : proveedor.getCategory())
                .build();
    }


    public ProveedorResponseDto toProveedorDto(Proveedor proveedor) {
        return new ProveedorResponseDto(
                proveedor.getIdProveedor(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getAddress(),
                proveedor.getDateOfHire(),
                proveedor.getCategory()
        );
    }
}