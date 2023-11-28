package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ClientNotificationDto;
import org.springframework.stereotype.Component;

/**
 * Mapper de la entidad Client
 */
@Component
public class ClientNotificationMapper {
    public ClientNotificationDto toClientNotificationDto(Client client) {
        return new ClientNotificationDto(
                client.getId(),
                client.getName(),
                client.getUsername(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getBirthdate().toString(),
                client.getImage(),
                client.getBalance(),
                client.getIsDeleted()
        );
    }
}
