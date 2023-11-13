package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ProveedorService {
    Page<ProveedorResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Pageable pageable);

    Proveedor findByUUID(String idProveedor);

    Proveedor save(ProveedorCreateDto proveedorCreateDto);

    Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor);

    void deleteByUUID(String idProveedor);
}

