package dev.clownsinformatics.tiendajava.websockets.notifications.dto;

import java.util.UUID;

public record CategoryNotificationDto(
        UUID uuid,
        String name
) {
}
