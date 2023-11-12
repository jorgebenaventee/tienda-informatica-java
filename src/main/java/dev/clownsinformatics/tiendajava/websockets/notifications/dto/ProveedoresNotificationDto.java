package dev.clownsinformatics.tiendajava.websockets.notifications.dto;

import java.util.UUID;

public record ProveedoresNotificationDto(
        UUID idProveedor,
        String name,
        Integer contact,
        String address,
        String dateOfHire,
        String category
) {
}
