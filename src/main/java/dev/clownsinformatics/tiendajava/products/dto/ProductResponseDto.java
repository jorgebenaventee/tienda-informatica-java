package dev.clownsinformatics.tiendajava.products.dto;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
