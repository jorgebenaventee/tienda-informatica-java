package dev.clownsinformatics.tiendajava.proveedores.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class Proveedor {
    private UUID idProveedor;
    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String nombre;
    @Min(value = 1, message = "El contacto del proveedor debe ser mayor a 0")
    private Integer contacto;
    @NotBlank(message = "La dirección del proveedor no puede estar vacía")
    private String direccion;
    @Builder.Default
    private LocalDate fechaContratacion = LocalDate.now();

    @JsonCreator
    public Proveedor(
            @JsonProperty("idProveedor") UUID idProveedor,
                     @JsonProperty("nombre") String nombre,
                     @JsonProperty("contacto") Integer contacto,
                     @JsonProperty("direccion") String direccion,
                     @JsonProperty("fechaContratacion") LocalDate fechaContratacion
    ) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.contacto = contacto;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
    }
}
