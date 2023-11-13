package dev.clownsinformatics.tiendajava.rest.proveedores.repositories;

import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, UUID>, JpaSpecificationExecutor<Proveedor> {

    Optional<Proveedor> getByIdProveedor(UUID idProveedor);

    List<Proveedor> getByNameContainingIgnoreCase(String name);

    List<Proveedor> getByAddressContainingIgnoreCase(String address);


    List<Proveedor> getByNameAndAddressContainingIgnoreCase(String name, String address);

    void deleteByIdProveedor(UUID idProveedor);

}

