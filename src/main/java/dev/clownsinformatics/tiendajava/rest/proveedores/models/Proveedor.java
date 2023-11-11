package dev.clownsinformatics.tiendajava.rest.proveedores.models;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @NotBlank(message = "El name del proveedor no puede estar vacío")
    private String name;
    @Column
    @Min(value = 1, message = "El contact del proveedor debe ser mayor a 0")
    private Integer contact;
    @Column
    @NotBlank(message = "La dirección del proveedor no puede estar vacía")
    private String address;
    @Column
    @Builder.Default
    private LocalDateTime dateOfHire = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
}
