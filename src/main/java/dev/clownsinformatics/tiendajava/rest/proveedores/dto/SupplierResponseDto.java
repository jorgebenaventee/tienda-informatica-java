package dev.clownsinformatics.tiendajava.rest.proveedores.dto;


import dev.clownsinformatics.tiendajava.rest.categories.models.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierResponseDto(
        UUID id,
        String name,
        Integer contact,
        String address,
        LocalDateTime dateOfHire,
        Category category
) {
}
