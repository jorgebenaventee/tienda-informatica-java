package dev.clownsinformatics.tiendajava.rest.products.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreateDto(
        @NotBlank(message = "The name cannot be blank")
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,

        @NotNull(message = "The weight cannot be null")
        @Min(value = 0, message = "The weight must be greater than 0")
        Double weight,

        @NotNull(message = "The price cannot be null")
        @Min(value = 0, message = "The price must be greater than 0")
        Double price,

        @NotBlank(message = "The image cannot be blank")
        String img,

        @NotNull(message = "The stock cannot be empty")
        @Min(value = 0, message = "The stock must be greater than 0")
        Integer stock,

        @NotBlank(message = "The description cannot be blank")
        @Length(min = 3, max = 100, message = "The description must be between 3 and 100 characters")
        String description,

        @NotNull(message = "The category cannot be empty")
        Category category
) {
}
