package dev.clownsinformatics.tiendajava.rest.products.models;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;

    @Min(value = 0, message = "The weight must be greater than or equal to 0")
    @Column(columnDefinition = "double default 0.0")
    @Builder.Default
    private Double weight = 0.0;

    @NotNull
    @Min(value = 0, message = "The price must be greater than or equal to 0")
    @Column(columnDefinition = "double default 0.0")
    @Builder.Default
    private Double price = 0.0;

    @NotBlank
    private String img;

    @NotNull
    @Min(value = 0, message = "The stock must be greater than or equal to 0")
    @Column(columnDefinition = "int default 0")
    @Builder.Default
    private Integer stock = 0;

    @NotBlank
    @Length(min = 3, max = 255, message = "The description must be between 3 and 255 characters")
    private String description;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /*@JsonCreator
    public Product(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("weight") Double weight,
            @JsonProperty("idCategory") UUID idCategory,
            @JsonProperty("price") Double price,
            @JsonProperty("img") String img,
            @JsonProperty("stock") Integer stock,
            @JsonProperty("description") String description,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.idCategory = idCategory;
        this.price = price;
        this.img = img;
        this.stock = stock;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }*/
}
