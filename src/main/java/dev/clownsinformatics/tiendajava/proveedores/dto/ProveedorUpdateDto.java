package dev.clownsinformatics.tiendajava.proveedores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProveedorUpdateDto(
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @Min(value = 0, message = "El contacto no puede ser negativo")
        String contacto,
        @NotBlank(message = "La direccion no puede estar vacia")
        String direccion
) {
}
