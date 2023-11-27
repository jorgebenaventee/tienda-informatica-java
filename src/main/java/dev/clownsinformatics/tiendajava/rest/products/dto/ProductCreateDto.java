package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreateDto(
        @NotBlank(message = "The name cannot be blank")
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        @Schema(description = "The name of the product", example = "PC Gamer")
        String name,

        @NotNull(message = "The weight cannot be null")
        @Min(value = 0, message = "The weight must be greater than 0")
        @Schema(description = "The weight of the product", example = "10.5")
        Double weight,

        @NotNull(message = "The price cannot be null")
        @Min(value = 0, message = "The price must be greater than 0")
        @Schema(description = "The price of the product", example = "100.0")
        Double price,

        @NotBlank(message = "The image cannot be blank")
        @Schema(description = "The image of the product", example = "img.jpg")
        String img,

        @NotNull(message = "The stock cannot be empty")
        @Min(value = 0, message = "The stock must be greater than 0")
        @Schema(description = "The stock of the product", example = "10")
        Integer stock,

        @NotBlank(message = "The description cannot be blank")
        @Length(min = 3, max = 100, message = "The description must be between 3 and 100 characters")
        @Schema(description = "The description of the product", example = "This is a PC Gamer")
        String description,

        @NotNull(message = "The category cannot be empty")
        @Schema(description = "The category of the product", example = "PORTATILES")
        Category category
) {
}
