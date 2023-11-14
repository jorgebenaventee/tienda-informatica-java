package dev.clownsinformatics.tiendajava.rest.suppliers.models;

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
@Table(name = "supplier")
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    @NotBlank(message = "The name cannot be empty")
    private String name;
    @Column
    @Min(value = 1, message = "The contact cannot be empty")
    private Integer contact;
    @Column
    @NotBlank(message = "The address cannot be empty")
    private String address;
    @Column
    @Builder.Default
    private LocalDateTime dateOfHire = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
}
