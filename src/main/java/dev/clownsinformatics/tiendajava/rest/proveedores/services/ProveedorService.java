package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;

import java.util.List;

public interface ProveedorService {
    List<Proveedor> findAll(String name, String address);

    Proveedor findByUUID(String idProveedor);

    Proveedor save(ProveedorCreateDto proveedorCreateDto);

    Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor);

    void deleteByUUID(String idProveedor);
}

