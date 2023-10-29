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
                .nombre(proveedorCreateDto.nombre())
                .contacto(proveedorCreateDto.contacto())
                .direccion(proveedorCreateDto.direccion())
                .build();
    }

    public Proveedor toProveedor(ProveedorUpdateDto proveedorUpdateDto, Proveedor proveedor) {
        return Proveedor.builder().idProveedor(proveedor.getIdProveedor())
                .nombre(proveedorUpdateDto.nombre() != null ? proveedorUpdateDto.nombre() : proveedor.getNombre())
                .contacto(proveedorUpdateDto.contacto() != null ? Integer.valueOf(proveedorUpdateDto.contacto()) : proveedor.getContacto())
                .direccion(proveedorUpdateDto.direccion() != null ? proveedorUpdateDto.direccion() : proveedor.getDireccion())
                .build();
    }


    public ProveedorCreateDto toProveedorDto(Proveedor proveedor) {
        return new ProveedorCreateDto(
                proveedor.getNombre(),
                proveedor.getContacto(),
                proveedor.getDireccion(
                )
        );
    }
}