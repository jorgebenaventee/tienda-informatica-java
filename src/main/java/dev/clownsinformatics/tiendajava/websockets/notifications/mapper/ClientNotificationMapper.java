package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ClientNotificationDto;
import org.springframework.stereotype.Component;


/**
 * Clase que proporciona la funcionalidad de mapeo entre la entidad {@code Client} y
 * el objeto de transferencia de datos {@code ClientNotificationDto}.
 * Esta clase utiliza el patrón de diseño Mapper para convertir instancias de la entidad
 * {@code Client} en instancias de {@code ClientNotificationDto}.
 */
@Component
public class ClientNotificationMapper {

    /**
     * Convierte una instancia de la entidad {@code Client} en un objeto
     * {@code ClientNotificationDto}.
     *
     * @param client La instancia de la entidad {@code Client} a ser convertida.
     * @return Un objeto {@code ClientNotificationDto} con la información mapeada.
     */
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
