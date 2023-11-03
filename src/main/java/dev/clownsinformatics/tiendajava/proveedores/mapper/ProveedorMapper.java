package dev.clownsinformatics.tiendajava.proveedores.mapper;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
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
                .build();
    }

    public Proveedor toProveedor(ProveedorUpdateDto proveedorUpdateDto, Proveedor proveedor) {
        return Proveedor.builder().idProveedor(proveedor.getIdProveedor())
                .name(proveedorUpdateDto.name() != null ? proveedorUpdateDto.name() : proveedor.getName())
                .contact(proveedorUpdateDto.contact() != null ? Integer.valueOf(proveedorUpdateDto.contact()) : proveedor.getContact())
                .address(proveedorUpdateDto.address() != null ? proveedorUpdateDto.address() : proveedor.getAddress())
                .build();
    }


    public ProveedorCreateDto toProveedorDto(Proveedor proveedor) {
        return new ProveedorCreateDto(
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getAddress(
                )
        );
    }
}