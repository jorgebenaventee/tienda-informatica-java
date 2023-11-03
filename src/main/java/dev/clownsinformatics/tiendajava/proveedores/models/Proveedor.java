package dev.clownsinformatics.tiendajava.proveedores.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "proveedores")
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProveedor;
    @Column
    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String nombre;
    @Column
    @Min(value = 1, message = "El contacto del proveedor debe ser mayor a 0")
    private Integer contacto;
    @Column
    @NotBlank(message = "La dirección del proveedor no puede estar vacía")
    private String direccion;
    @Column
    @Builder.Default
    private LocalDateTime fechaContratacion = LocalDateTime.now();

}
