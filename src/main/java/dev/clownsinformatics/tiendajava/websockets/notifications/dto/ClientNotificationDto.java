package dev.clownsinformatics.tiendajava.websockets.notifications.dto;

/**
 * DTO para enviar notificaciones de clientes
 * @param id
 * @param name
 * @param username
 * @param email
 * @param address
 * @param phone
 * @param birthdate
 * @param image
 * @param balance
 * @param isDeleted
 */
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
