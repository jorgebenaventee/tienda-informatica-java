package dev.clownsinformatics.tiendajava.proveedores.dto;


import java.time.LocalDate;
import java.util.UUID;

public record ProveedorResponseDto(
        UUID idEmpresa,
        Long idProveedor,
        String nombre,
        Integer contacto,
        String direccion,
        LocalDate fechaContratacion
) {
}
