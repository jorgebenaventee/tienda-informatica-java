package dev.clownsinformatics.tiendajava.products.dto;

import dev.clownsinformatics.tiendajava.products.models.Categories;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductUpdateDto(
        @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String name,

        @Min(value = 0, message = "El peso debe ser mayor a 0")
        Double weight,

        Categories category,

        @Min(value = 0, message = "El precio debe ser mayor a 0")
        Double price,

        @Min(value = 0, message = "El id de la categoría debe ser mayor a 0")
        Long idCategory,

        String img,

        @Min(value = 0, message = "El stock debe ser mayor a 0")
        Integer stock,

        @Length(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
        String description
) {
}
