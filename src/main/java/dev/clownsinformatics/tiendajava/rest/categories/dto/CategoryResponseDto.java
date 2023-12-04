package dev.clownsinformatics.tiendajava.rest.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

public record CategoryResponseDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        @Schema(description = "The name of the category", example = "PORTATILES")
        String name,
        @Schema(description = "The description of the category", example = "Portatiles de todas las marcas")
        Boolean isDeleted
) {
}
