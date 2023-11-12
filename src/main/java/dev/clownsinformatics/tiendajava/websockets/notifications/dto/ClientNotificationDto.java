package dev.clownsinformatics.tiendajava.websockets.notifications.dto;

public record ClientNotificationDto(
        Long id,
        String name,
        String username,
        String email,
        String address,
        String phone,
        String birthdate,
        String image,
        Double balance,
        Boolean isDeleted

) {
}
