package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;

import java.util.List;

public interface ProveedorService {
    List<Proveedor> findAll(String nombre, String direccion);

    Proveedor findById(Long id);

    Proveedor findByUUID(String idEmpresa);

    Proveedor save(ProveedorCreateDto proveedorCreateDto);

    Proveedor update(ProveedorUpdateDto proveedorUpdateDto, Long idProveedor);

    void deleteById(Long idProveedor);
}

