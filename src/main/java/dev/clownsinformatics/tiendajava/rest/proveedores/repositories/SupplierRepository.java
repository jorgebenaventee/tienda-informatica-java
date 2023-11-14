package dev.clownsinformatics.tiendajava.rest.proveedores.repositories;

import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID>, JpaSpecificationExecutor<Supplier> {

    List<Supplier> getByNameContainingIgnoreCase(String name);

    List<Supplier> getByAddressContainingIgnoreCase(String address);


    List<Supplier> getByNameAndAddressContainingIgnoreCase(String name, String address);


}

