package dev.clownsinformatics.tiendajava.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ProductCreateDto(
        @NotBlank(message = "El nombre no puede estar vacio")
        @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String name,

        @NotNull(message = "El peso no puede estar vacio")
        @Min(value = 0, message = "El peso debe ser mayor a 0")
        Double weight,

        UUID idCategory,

        @NotNull(message = "El precio no puede estar vacio")
        @Min(value = 0, message = "El precio debe ser mayor a 0")
        Double price,

        @NotBlank(message = "La imagen no puede estar vacia")
        String img,

        @NotNull(message = "El stock no puede estar vacio")
        @Min(value = 0, message = "El stock debe ser mayor a 0")
        Integer stock,

        @NotBlank(message = "La descripcion no puede estar vacia")
        @Length(min = 3, max = 100, message = "La descripcion debe tener entre 3 y 100 caracteres")
        String description
) {
}
