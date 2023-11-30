package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ProductUpdateDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        @Schema(description = "The product's name", example = "PC Gamer")
        String name,

        @Min(value = 0, message = "The weight must be greater than 0")
        @Schema(description = "The product's weight", example = "1.5")
        Double weight,

        @Min(value = 0, message = "The price must be greater than 0")
        @Schema(description = "The product's price", example = "150.0")
        Double price,

        @Schema(description = "The product's image", example = "img.jpg")
        String img,

        @Min(value = 0, message = "The stock must be greater than 0")
        @Schema(description = "The product's stock", example = "10")
        Integer stock,

        @Length(min = 3, max = 100, message = "The description must be between 3 and 100 characters")
        @Schema(description = "The product's description", example = "This is a PC Gamer")
        String description,

        @Schema(description = "The product's category", example = "PORTATILES")
        Category category,

        Supplier supplier
) {
}
