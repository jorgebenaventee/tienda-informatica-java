package dev.clownsinformatics.tiendajava.products.dto;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ProductUpdateDto(
        @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String name,

        @Min(value = 0, message = "El peso debe ser mayor a 0")
        Double weight,


        UUID idCategory,

        @Min(value = 0, message = "El precio debe ser mayor a 0")
        Double price,

        String img,

        @Min(value = 0, message = "El stock debe ser mayor a 0")
        Integer stock,

        @Length(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
        String description
) {
}
