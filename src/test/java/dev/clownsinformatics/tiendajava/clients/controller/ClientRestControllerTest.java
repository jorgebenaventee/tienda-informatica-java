package dev.clownsinformatics.tiendajava.clients.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.exceptions.ClientNotFound;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientServiceImpl;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
public class ClientRestControllerTest {

    private final String ENDPOINT_URL = "/api/clients";

    private final ObjectMapper mapper = new ObjectMapper();

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

    List<Client> clients = List.of(
            Client.builder()
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
                    .username("ana isabeñ")
                    .build()
    );

    @MockBean
    private final ClientServiceImpl clientService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    public ClientRestControllerTest(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }


    @Test
    public void testGetAllClients() throws Exception {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ClientResponse> page = new PageImpl<>(clientsResponse);

        when(clientService.findAll(any(), any(), any(Pageable.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ClientResponse> clients = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientResponse.class));

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(clientsResponse.size(), clients.content().size()),
                ()-> assertEquals(clientsResponse.get(0).id(), clients.content().get(0).id()),
                ()-> assertEquals(clientsResponse.get(0).name(), clients.content().get(0).name()),
                ()-> assertEquals(clientsResponse.get(0).address(), clients.content().get(0).address()),
                ()-> assertEquals(clientsResponse.get(1).id(), clients.content().get(1).id()),
                ()-> assertEquals(clientsResponse.get(1).name(), clients.content().get(1).name()),
                ()-> assertEquals(clientsResponse.get(1).address(), clients.content().get(1).address())
        );


    }


    @Test
    public void testGetClientById() throws Exception {

        when(clientService.findById(any(Long.class))).thenReturn(clientsResponse.get(0));

        MockHttpServletResponse response = mockMvc.perform(
                get(ENDPOINT_URL+"/1")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ClientResponse client = mapper.readValue(response.getContentAsString(), ClientResponse.class);

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(clientsResponse.get(0).id(), client.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), client.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), client.address())
        );

    }


    @Test
    public void testCreateClient() throws Exception {

        when(clientService.save(any(ClientCreateRequest.class))).thenReturn((clientsResponse.get(0)));

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clients.get(0)))
        ).andReturn().getResponse();
        ClientResponse client = mapper.readValue(response.getContentAsString(), ClientResponse.class);

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(clientsResponse.get(0).id(), client.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), client.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), client.address())
        );

        verify(clientService, times(1)).save(any(ClientCreateRequest.class));

    }

    @Test
    public void testUpdateClient() throws Exception {
        when(clientService.update(any(Long.class), any(ClientUpdateRequest.class))).thenReturn((clientsResponse.get(0)));

        MockHttpServletResponse response = mockMvc.perform(
                put(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(mapper.writeValueAsString(clients.get(0)))
        ).andReturn().getResponse();
        ClientResponse client = mapper.readValue(response.getContentAsString(), ClientResponse.class);

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(clientsResponse.get(0).id(), client.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), client.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), client.address())
        );

        verify(clientService, times(1)).update(any(Long.class), any(ClientUpdateRequest.class));

    }

    @Test
    public void testDeleteClient() throws Exception {

        doNothing().when(clientService).deleteById(any(Long.class));

        MockHttpServletResponse response = mockMvc.perform(
                delete(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(204, response.getStatus())
        );

        verify(clientService, times(1)).deleteById(anyLong());

    }

    @Test
    public void testDeleteClientNotFound() throws Exception {

        doThrow(new ClientNotFound(1L)).when(clientService).deleteById(any(Long.class));

        MockHttpServletResponse response = mockMvc.perform(
                delete(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(404, response.getStatus())
        );

        verify(clientService, times(1)).deleteById(anyLong());

    }


}
