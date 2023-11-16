package dev.clownsinformatics.tiendajava.rest.clients.mappers;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClientMapper {


    public Client toClient(ClientCreateRequest clientCreateRequest) {
        return Client.builder()
                .name(clientCreateRequest.name())
                .username(clientCreateRequest.username())
                .email(clientCreateRequest.email())
                .address(clientCreateRequest.address())
                .phone(clientCreateRequest.phone())
                .birthdate(LocalDate.parse(clientCreateRequest.birthdate()))
                .image(clientCreateRequest.image())
                .balance(clientCreateRequest.balance())
                .isDeleted(clientCreateRequest.isDeleted() != null ? clientCreateRequest.isDeleted() : Boolean.FALSE)
                .build();
    }

    public Client toClient(ClientResponse clientResponse) {
        return Client.builder()
                .name(clientResponse.name())
                .username(clientResponse.username())
                .email(clientResponse.email())
                .address(clientResponse.address())
                .phone(clientResponse.phone())
                .birthdate(clientResponse.birthdate())
                .image(clientResponse.image())
                .balance(clientResponse.balance())
                .isDeleted(clientResponse.isDeleted() != null ? clientResponse.isDeleted() : Boolean.FALSE)
                .createdAt(clientResponse.createdAt())
                .updatedAt(clientResponse.updatedAt())
                .build();
    }

    public ClientResponse toClientResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getUsername(),
                client.getName(),
                client.getBalance(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getBirthdate(),
                client.getImage(),
                client.getIsDeleted(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }


}
