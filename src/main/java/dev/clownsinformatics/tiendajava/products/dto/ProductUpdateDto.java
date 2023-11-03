package dev.clownsinformatics.tiendajava.products.dto;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ProductUpdateDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,

        @Min(value = 0, message = "The weight must be greater than 0")
        Double weight,

        @Min(value = 0, message = "The price must be greater than 0")
        Double price,

        String img,

        @Min(value = 0, message = "The stock must be greater than 0")
        Integer stock,

        @Length(min = 3, max = 100, message = "The description must be between 3 and 100 characters")
        String description
) {
}
