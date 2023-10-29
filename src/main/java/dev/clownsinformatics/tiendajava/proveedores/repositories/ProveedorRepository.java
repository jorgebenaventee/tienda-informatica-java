package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProveedorRepository {

    List<Proveedor> getAll();

    Optional<Proveedor> getByUUID(UUID idProveedor);

    List<Proveedor> getByNombre(String nombre);

    List<Proveedor> getByDireccion(String direccion);

    List<Proveedor> getByNombreAndDireccion(String nombre, String direccion);

    Proveedor save(Proveedor proveedores);

    Proveedor update(Proveedor proveedores);

    void deleteByUUID(UUID idProveedor);


}

