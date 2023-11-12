package dev.clownsinformatics.tiendajava.rest.clients.mappers;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {


    public Client toClient(ClientCreateRequest clientCreateRequest) {
        return Client.builder()
                .name(clientCreateRequest.name())
                .username(clientCreateRequest.username())
                .email(clientCreateRequest.email())
                .address(clientCreateRequest.address())
                .phone(clientCreateRequest.phone())
                .birthdate(clientCreateRequest.birthdate())
                .image(clientCreateRequest.image())
                .balance(clientCreateRequest.balance())
                .isDeleted(clientCreateRequest.isDeleted())
                .build();
    }

    public Client toClient(ClientUpdateRequest clientUpdateRequest) {
        return Client.builder()
                .name(clientUpdateRequest.name())
                .username(clientUpdateRequest.username())
                .email(clientUpdateRequest.email())
                .address(clientUpdateRequest.address())
                .phone(clientUpdateRequest.phone())
                .birthdate(clientUpdateRequest.birthdate())
                .image(clientUpdateRequest.image())
                .balance(clientUpdateRequest.balance())
                .isDeleted(clientUpdateRequest.isDeleted())
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
                client.getIsDeleted()
        );
    }



}
