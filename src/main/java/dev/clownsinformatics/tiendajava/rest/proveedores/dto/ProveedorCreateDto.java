package dev.clownsinformatics.tiendajava.rest.proveedores.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProveedorCreateDto(
        @NotBlank(message = "El name no puede estar vacio")
        String name,
        @Min(value = 0, message = "El contact no puede ser negativo")
        Integer contact,
        @NotBlank(message = "La address no puede estar vacia")
        String address,
        @NotNull
        Category category
) {
}
