package dev.clownsinformatics.tiendajava.rest.proveedores.services;

import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SupplierService {
    Page<SupplierResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Pageable pageable);

    SupplierResponseDto findByUUID(String idProveedor);

    SupplierResponseDto save(SupplierCreateDto proveedorCreateDto);

    SupplierResponseDto update(SupplierUpdateDto proveedorUpdateDto, String idProveedor);

    void deleteByUUID(String idProveedor);
}

