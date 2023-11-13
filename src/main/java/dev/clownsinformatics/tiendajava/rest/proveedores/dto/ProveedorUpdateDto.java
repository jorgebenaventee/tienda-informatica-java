package dev.clownsinformatics.tiendajava.rest.proveedores.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ProveedorUpdateDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,
        @Min(value = 0, message = "The contact must be greater than 0")
        Integer contact,
        @Length(min = 2, max = 50, message = "The address must be between 2 and 50 characters")
        String address,
        Category category
) {
}
