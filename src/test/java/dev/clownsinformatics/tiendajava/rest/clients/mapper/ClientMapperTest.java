package dev.clownsinformatics.tiendajava.rest.clients.mapper;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMapperTest {


    ClientMapper clientMapper = new ClientMapper();

    Client client = Client.builder()
            .name("Client 1")
            .username("client1")
            .email("")
            .address("")
            .phone("")
            .birthdate(null)
            .image(null)
            .balance(0.0)
            .isDeleted(false)
            .build();

    @Test
    void toClientResponse() {

        ClientResponse clientResponse = clientMapper.toClientResponse(client);

        assertAll(
                () -> assertEquals(clientResponse.name(), client.getName()),
                () -> assertEquals(clientResponse.username(), client.getUsername()),
                () -> assertEquals(clientResponse.email(), client.getEmail()),
                () -> assertEquals(clientResponse.address(), client.getAddress())
        );

    }

    @Test
    void toClientFromClientResponse() {

        ClientResponse clientResponse = ClientResponse.builder()
                .name("Client 1")
                .username("client1")
                .email("")
                .address("")
                .phone("")
                .birthdate(null)
                .image(null)
                .balance(0.0)
                .isDeleted(false)
                .build();
        Client clientFromResponse = clientMapper.toClient(clientResponse);

        assertAll(
                () -> assertEquals(clientFromResponse.getName(), client.getName())
        );

    }

    @Test
    void toClientFromClientCreateRequest() {

        ClientCreateRequest client = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "",
                "",
                null,
                LocalDate.now().toString(),
                "",
                false
        );

        Client clientFromResponse = clientMapper.toClient(client);

        assertAll(
                () -> assertEquals(clientFromResponse.getName(), client.name())
        );

    }


    @Test
    void toClientFromClientUpdateRequest() {

        ClientCreateRequest client = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "",
                "",
                null,
                LocalDate.now().toString(),
                "",
                false
        );

        Client clientFromResponse = clientMapper.toClient(client);

        assertAll(
                () -> assertEquals(clientFromResponse.getName(), client.name())
        );

    }




}
