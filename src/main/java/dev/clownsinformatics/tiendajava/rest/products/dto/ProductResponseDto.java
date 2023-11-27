package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        @Schema(description = "Product id", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Product name", example = "PC Gamer")
        String name,
        @Schema(description = "Product weight", example = "10.5")
        Double weight,
        @Schema(description = "Product price", example = "100.0")
        Double price,
        @Schema(description = "Product image", example = "img.jpg")
        String img,
        @Schema(description = "Product stock", example = "10")
        Integer stock,
        @Schema(description = "Product description", example = "PC Gamer")
        String description,
        @Schema(description = "Product category", example = "PORTATILES")
        Category category,
        @Schema(description = "Product created at", example = "2021-10-10T10:10:10")
        LocalDateTime createdAt,
        @Schema(description = "Product updated at", example = "2021-10-10T10:10:10")
        LocalDateTime updatedAt
) {
}
