package dev.clownsinformatics.tiendajava.products.dto;

import dev.clownsinformatics.tiendajava.products.models.Categories;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        Long id,
        UUID uuid,
        String name,
        Double weight,
        Categories category,
        Double price,
        Long idCategory,
        String img,
        Integer stock,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
