package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isDeleted
) {
}
