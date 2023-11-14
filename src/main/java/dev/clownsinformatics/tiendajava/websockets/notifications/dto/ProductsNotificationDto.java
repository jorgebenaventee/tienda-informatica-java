package dev.clownsinformatics.tiendajava.websockets.notifications.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;

import java.util.UUID;

public record ProductsNotificationDto(
        UUID idProduct,
        String name,
        Double weight,
        Double price,
        String img,
        Integer stock,
        String description,
        Category category
) {
}
