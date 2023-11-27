package dev.clownsinformatics.tiendajava.rest.suppliers.models;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "supplier")
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "The id of the supplier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column
    @NotBlank(message = "The name cannot be empty")
    @Schema(description = "The name of the supplier", example = "Eva")
    private String name;

    @Column
    @Min(value = 1, message = "The contact cannot be empty")
    @Schema(description = "The contact of the supplier", example = "123456789")
    private Integer contact;

    @Column
    @NotBlank(message = "The address cannot be empty")
    @Schema(description = "The address of the supplier", example = "Calle 123")
    private String address;
    @Column
    @Builder.Default
    @Schema(description = "The date of hire of the supplier", example = "2021-10-10T00:00:00")
    private LocalDateTime dateOfHire = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    @Schema(description = "The category that the supplier have", example = "PORTATILES")
    private Category category;

    @Schema(description = "The status of the supplier", example = "true")
    Boolean isDeleted;
}
