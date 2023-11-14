package dev.clownsinformatics.tiendajava.rest.suppliers.services;

import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SupplierService {
    Page<SupplierResponseDto> findAll(Optional<String> category, Optional<String> name, Optional<Integer> contact, Pageable pageable);

    SupplierResponseDto findByUUID(String id);

    SupplierResponseDto save(SupplierCreateDto supplierCreateDto);

    SupplierResponseDto update(SupplierUpdateDto supplierUpdateDto, String idSupplier);

    void deleteByUUID(String idSupplier);
}

