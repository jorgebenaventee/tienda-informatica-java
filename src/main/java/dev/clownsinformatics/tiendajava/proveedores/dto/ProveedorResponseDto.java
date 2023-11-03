package dev.clownsinformatics.tiendajava.proveedores.dto;



import java.time.LocalDate;
import java.util.UUID;

public record ProveedorResponseDto(
        UUID idProveedor,
        String nombre,
        Integer contacto,
        String direccion,
        LocalDate fechaContratacion
) {
}
