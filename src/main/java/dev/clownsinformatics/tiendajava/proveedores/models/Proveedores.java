package dev.clownsinformatics.tiendajava.proveedores.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class Proveedores {
    private UUID idEmpresa;
    @Min(value = 1, message = "El id del proveedor debe ser mayor a 0")
    private Long idProveedor;
    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String nombre;
    @Min(value = 1, message = "El contacto del proveedor debe ser mayor a 0")
    private Integer contacto;
    @NotBlank(message = "La dirección del proveedor no puede estar vacía")
    private String direccion;
    @Builder.Default
    private LocalDate fechaContratacion = LocalDate.now();
}
