package dev.clownsinformatics.tiendajava.rest.proveedores.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProveedorCreateDto(
        @NotBlank(message = "The name cannot be blank")
        String name,
        @Min(value = 0, message = "The contact cannot be less than 0")
        Integer contact,
        @NotBlank(message = "The address cannot be blank")
        String address,
        @NotNull(message = "The category cannot be null")
        Category category
) {
}
