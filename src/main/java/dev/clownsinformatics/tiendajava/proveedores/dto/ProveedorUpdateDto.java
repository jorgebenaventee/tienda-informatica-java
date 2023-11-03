package dev.clownsinformatics.tiendajava.proveedores.dto;

import dev.clownsinformatics.tiendajava.categories.models.Category;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ProveedorUpdateDto(
        @Length(min = 3, max = 50, message = "El name debe tener entre 3 y 50 caracteres")
        String name,
        @Min(value = 0, message = "El contact no puede ser negativo")
        String contact,
        @Length(min = 2, max = 50, message = "La dirección debe tener mas de 2 caracteres")
        String address,
        Category category
) {
}
