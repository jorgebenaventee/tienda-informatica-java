package dev.clownsinformatics.tiendajava.products.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Product {
    private UUID id;
    private String name;
    private Double weight;
    private UUID idCategory;
    private Double price;
    private String img;
    private Integer stock;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
