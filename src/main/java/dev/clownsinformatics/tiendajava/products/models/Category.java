package dev.clownsinformatics.tiendajava.products.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Category {
    private String name;
    private UUID uuid = UUID.randomUUID();

    public Category(String name) {
        this.name = name;
    }
}
