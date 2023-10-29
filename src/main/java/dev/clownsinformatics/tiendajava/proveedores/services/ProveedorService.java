package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;

import java.util.List;
import java.util.UUID;

public interface ProveedorService {
    List<Proveedor> findAll(String nombre, String direccion);

    Proveedor findByUUID(String idProveedor);

    Proveedor save(ProveedorCreateDto proveedorCreateDto);

    Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor);

    void deleteByUUID(String idProveedor);
}

