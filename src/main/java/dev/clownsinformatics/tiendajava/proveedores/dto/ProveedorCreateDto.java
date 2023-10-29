package dev.clownsinformatics.tiendajava.proveedores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProveedorCreateDto(
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @Min(value = 0, message = "El contacto no puede ser negativo")
        Integer contacto,
        @NotBlank(message = "La direccion no puede estar vacia")
        String direccion
) {
}
