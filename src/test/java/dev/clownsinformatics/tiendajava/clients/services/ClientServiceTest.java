package dev.clownsinformatics.tiendajava.clients.services;


import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientService;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientServiceImpl;
import dev.clownsinformatics.tiendajava.rest.storage.services.FileSystemStorageService;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ClientNotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {


    List<Client> clients = List.of(
            Client.builder()
                    .id(1L)
                    .name("Client 1")
                    .address("Address 1")
                    .email("juancarlos@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image("antigua")
                    .isDeleted(false)
                    .balance(0.0)
                    .username("juancarlos")
                    .build(),
            Client.builder()
                    .id(2L)
                    .name("Client 2")
                    .address("Address 2")
                    .email("ana@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image(null)
                    .isDeleted(false)
                    .balance(0.0)
                    .username("ana isabel")
                    .build()
    );

    List<ClientResponse> clientsResponse = List.of(
            ClientResponse.builder()
                    .id(1L)
                    .name("Client 1")
                    .address("Address 1")
                    .email("juancarlos@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image(null)
                    .isDeleted(false)
                    .balance(0.0)
                    .username("juancarlos")
                    .build(),
            ClientResponse.builder()
                    .id(2L)
                    .name("Client 2")
                    .address("Address 2")
                    .email("ana@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image(null)
                    .isDeleted(false)
                    .balance(0.0)
                    .username("ana isabeñ")
                    .build()
    );

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private FileSystemStorageService fileSystemStorageService;

    @Mock
    private WebSocketConfig webSocketConfig;

    @Mock
    private ClientNotificationMapper clientNotificationMapper;

    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);

    @Test
    void findAllTest() {



    }

    @Test
    void findByIdTest() {

        when(clientRepository.findByIdAndIsDeletedFalse(anyLong())).thenReturn(Optional.of(clients.get(0)));
        when(clientMapper.toClientResponse(clients.get(0))).thenReturn(clientsResponse.get(0));

        ClientResponse clientResponse = clientService.findById(1L);

        assertAll(
                ()-> assertEquals(clientsResponse.get(0), clientResponse),
                ()-> assertEquals(clientsResponse.get(0).id(), clientResponse.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), clientResponse.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), clientResponse.address())
        );

        verify(clientRepository, times(1)).findByIdAndIsDeletedFalse(anyLong());

    }

    @Test
    void saveTest() throws IOException {

        when(clientRepository.save(any(Client.class))).thenReturn(clients.get(0));
        when(clientMapper.toClientResponse(clients.get(0))).thenReturn(clientsResponse.get(0));
        when(clientMapper.toClient(any(ClientCreateRequest.class))).thenReturn(clients.get(0));
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "Address 1",
                0.0,
                "juancarlos",
                "si",
                "23123123",
                LocalDate.now(),
                null,
                false
        );

        ClientResponse clientResponse = clientService.save(clientCreateRequest);

        assertAll(
                ()-> assertEquals(clientsResponse.get(0), clientResponse),
                ()-> assertEquals(clientsResponse.get(0).id(), clientResponse.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), clientResponse.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), clientResponse.address())
        );

    }




}
