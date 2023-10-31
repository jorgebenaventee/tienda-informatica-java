package dev.clownsinformatics.tiendajava.products.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Product {
    @NotNull
    private final UUID id;
    @NotEmpty
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @NotNull
    @Min(value = 0, message = "El peso debe ser mayor o igual a 0")
    private Double weight;
    @NotNull
    private UUID idCategory;
    @NotNull
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Double price;
    @NotEmpty
    private String img;
    @NotNull
    private Integer stock;
    @NotEmpty
    @Length(min = 3, max = 255, message = "La descripcion debe tener entre 3 y 255 caracteres")
    private String description;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private LocalDateTime updatedAt;

    @JsonCreator
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
    }
}
