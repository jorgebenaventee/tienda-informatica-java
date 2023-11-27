package dev.clownsinformatics.tiendajava.rest.products.models;

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
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "PRODUCTS")
public class Product {
    public static final String IMAGE_DEFAULT = "https://placehold.co/600x400";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Product's id", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    @Schema(description = "Product's name", example = "PC Gamer")
    private String name;

    @Min(value = 0, message = "The weight must be greater than or equal to 0")
    @Builder.Default
    @Schema(description = "Product's weight", example = "10.0")
    private Double weight = 0.0;

    @NotNull
    @Min(value = 0, message = "The price must be greater than or equal to 0")
    @Builder.Default
    @Schema(description = "Product's price", example = "100.0")
    private Double price = 0.0;

    @NotBlank
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "Product's image", example = "img.jpg")
    private String img = IMAGE_DEFAULT;

    @NotNull
    @Min(value = 0, message = "The stock must be greater than or equal to 0")
    @Builder.Default
    @Schema(description = "Product's stock", example = "10")
    private Integer stock = 0;

    @NotBlank
    @Length(min = 3, max = 255, message = "The description must be between 3 and 255 characters")
    @Schema(description = "Product's description", example = "This is a PC Gamer")
    private String description;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    @Builder.Default
    @Schema(description = "Product's creation date", example = "2021-10-10T10:10:10")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = false)
    @Builder.Default
    @Schema(description = "Product's last update date", example = "2021-10-10T10:10:10")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Product's category", example = "PORTATILES")
    private Category category;
}

