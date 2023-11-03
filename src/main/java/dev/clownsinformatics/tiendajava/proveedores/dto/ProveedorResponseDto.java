package dev.clownsinformatics.tiendajava.proveedores.dto;


import dev.clownsinformatics.tiendajava.categories.models.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProveedorResponseDto(
        UUID idProveedor,
        String name,
        Integer contact,
        String address,
        LocalDateTime dateOfHire,
        Category category
) {
}
