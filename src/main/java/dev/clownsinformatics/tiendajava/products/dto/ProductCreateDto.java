package dev.clownsinformatics.tiendajava.products.dto;

import dev.clownsinformatics.tiendajava.products.models.Categories;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreateDto (
    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    String name,

    @NotNull(message = "El peso no puede estar vacío")
    @Min(value = 0, message = "El peso debe ser mayor a 0")
    Double weight,

    @NotNull(message = "La categoría no puede estar vacía")
    @Min(value = 0, message = "La categoría debe ser mayor a 0")
    Categories category,

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 0, message = "El precio debe ser mayor a 0")
    Double price,

    @NotBlank(message = "La imagen no puede estar vacía")
    @Min(value = 0, message = "La imagen debe ser mayor a 0")
    String img,

    @NotNull(message = "El stock no puede estar vacío")
    @Min(value = 0, message = "El stock debe ser mayor a 0")
    Integer stock,

    @NotBlank(message = "La descripción no puede estar vacía")
    @Length(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
    String description
) {
}
