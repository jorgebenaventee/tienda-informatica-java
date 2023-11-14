package dev.clownsinformatics.tiendajava.clients.controller;


import dev.clownsinformatics.tiendajava.rest.clients.controllers.ClientRestController;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientServiceImpl;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientRestControllerTest {

    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils;

    @InjectMocks
    private ClientRestController clientRestController;

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



    @Test
    public void testCreateClient() {

        when(clientService.save(any(ClientCreateRequest.class))).thenReturn(clientsResponse.get(0));

        ResponseEntity<ClientResponse> response = clientRestController.createClient(new ClientCreateRequest(
                "Client 1",
                "juancarlos",
                0.0,
                "Address 1",
                "Address 2",
                "123456789",
                null,
                null,
                false
        ));

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(clientsResponse.get(0), response.getBody())
        );

    }

    @Test
    public void testUpdateClient() {

        when(clientService.update(any(Long.class), any(ClientUpdateRequest.class))).thenReturn(clientsResponse.get(0));

        ResponseEntity<ClientResponse> response = clientRestController.updateClient(1L, new ClientUpdateRequest(
                "Client 1",
                "juancarlos",
                0.0,
                "Address 1",
                "Address 2",
                "123456789",
                null,
                null,
                false
        ));

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(clientsResponse.get(0), response.getBody())
        );

    }

    @Test
    public void testGetClient() {

        when(clientService.findById(any(Long.class))).thenReturn(clientsResponse.get(0));

        ResponseEntity<ClientResponse> response = clientRestController.getClient(1L);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(clientsResponse.get(0), response.getBody())
        );

    }

    @Test
    public void testGetAllClients() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ClientResponse> page = new PageImpl<>(clientsResponse);

        when(clientService.findAll(any(), any(), any(Pageable.class))).thenReturn(page);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/clients/"));

        ResponseEntity<PageResponse<ClientResponse>> response = clientRestController.getAllClients(null, "false", 0, 10, "id", "asc", requestMock);

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(clientsResponse.size(), response.getBody().pageSize())
        );

    }

    @Test
    public void testDeleteClient() {
        doNothing().when(clientService).deleteById(any(Long.class));
        ResponseEntity<Void> response = clientRestController.deleteClient(1L);
        assertAll(
                () -> assertEquals(204, response.getStatusCodeValue()),
                () -> assertNull(response.getBody())
        );
    }

    @Test
    public void testUpdateImage() {
        when(clientService.updateImage(any(Long.class), any(MultipartFile.class), any(Boolean.class))).thenReturn(clientsResponse.get(0));

        MultipartFile multipartFile = mock(MultipartFile.class);

        ResponseEntity<ClientResponse> response = clientRestController.updateImage(1L, multipartFile, Optional.of(true));
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(clientsResponse.get(0), response.getBody())
        );

    }


}
