package dev.clownsinformatics.tiendajava.websockets.notifications.dto;


import java.util.UUID;

public record SuppliersNotificationDto(
        UUID idProveedor,
        String name,
        Integer contact,
        String address,
        String dateOfHire,
        String category
) {
}
