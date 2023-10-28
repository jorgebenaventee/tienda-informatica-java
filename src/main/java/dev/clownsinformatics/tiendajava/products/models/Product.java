package dev.clownsinformatics.tiendajava.products.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private UUID uuid;
    private String name;
    private Double weight;
    private Categories category;
    private Double price;
    private Long idCategory;
    private String img;
    private Integer stock;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
