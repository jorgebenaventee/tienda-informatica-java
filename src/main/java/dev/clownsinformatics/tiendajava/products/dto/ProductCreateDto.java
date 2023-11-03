package dev.clownsinformatics.tiendajava.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ProductCreateDto(
        @NotBlank(message = "The name cannot be empty")
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,

        @NotNull(message = "The category cannot be empty")
        @Min(value = 0, message = "The category must be greater than 0")
        Double weight,

        @NotNull(message = "The category cannot be empty")
        @Min(value = 0, message = "The category must be greater than 0")
        Double price,

        @NotBlank(message = "The category cannot be empty")
        String img,

        @NotNull(message = "The category cannot be empty")
        @Min(value = 0, message = "The category must be greater than 0")
        Integer stock,

        @NotBlank(message = "The category cannot be empty")
        @Length(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
        String description
) {
}
