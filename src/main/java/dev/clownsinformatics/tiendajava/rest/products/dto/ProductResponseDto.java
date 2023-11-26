package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        Double weight,
        Double price,
        String img,
        Integer stock,
        String description,
        Category category,
        Supplier supplier,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isDeleted
) {
}
