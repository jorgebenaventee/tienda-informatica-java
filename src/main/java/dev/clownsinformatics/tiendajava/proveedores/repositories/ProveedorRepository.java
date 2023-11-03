package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, UUID> {


    Optional<Proveedor> getByIdProveedor(UUID idProveedor);

    List<Proveedor> getByNombreContainingIgnoreCase(String nombre);

    List<Proveedor> getByDireccionContainingIgnoreCase(String direccion);

    List<Proveedor> getByNombreAndDireccionContainingIgnoreCase(String nombre, String direccion);

    void deleteByIdProveedor(UUID idProveedor);

}

