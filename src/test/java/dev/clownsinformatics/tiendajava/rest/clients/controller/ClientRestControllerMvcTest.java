package dev.clownsinformatics.tiendajava.rest.clients.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})

public class ClientRestControllerMvcTest {

    private final String ENDPOINT_URL = "/api/clients";

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


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
                    .birthdate(LocalDate.now())
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
                    .birthdate(LocalDate.now())
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
    public ClientRestControllerMvcTest(ClientServiceImpl clientService) {
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
    public void testGetAllClientsWithParams() throws Exception {

        Pageable pageable = PageRequest.of(1, 2, Sort.by("id").ascending());
        Page<ClientResponse> page = new PageImpl<>(clientsResponse);

        when(clientService.findAll(any(), any(), any(Pageable.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(ENDPOINT_URL+"/?username=clien&page=1&size=1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ClientResponse> clients = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientResponse.class));

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(page.getTotalPages(), clients.totalPages()),
                ()-> assertEquals(page.getTotalElements(), clients.totalElements()),
                ()-> assertEquals(page.getNumber(), clients.pageNumber()),
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
    public void testFindClientByIdNotFound() throws Exception {

        when(clientService.findById(any(Long.class))).thenThrow(new ClientNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                get(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();



        assertAll(
                ()-> assertEquals(404, response.getStatus())
        );

    }


    @Test
    public void testCreateClient() throws Exception {

        when(clientService.save(any(ClientCreateRequest.class))).thenReturn((clientsResponse.get(0)));

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateRequest))
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
    public void testCreateClientWithBadPhone() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "12345678",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"phone\":\"Phone number must have 9 digits.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBadEmail() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "juangmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"email\":\"Email must have a valid format.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBadName() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"name\":\"Name can not be empty.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBalanceNegative() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                -1.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"balance\":\"Balance must be positive or zero.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBadUsername() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "",
                "client1",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"username\":\"User can not be empty.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBadAddress() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"address\":\"Address can not be empty.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testCreateClientWithBadBirthdate() throws Exception {

        ClientCreateRequest clientCreateRequest = new ClientCreateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "asdasdasd",
                "123456789",
                "22-22-22",
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                post(ENDPOINT_URL+"/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(400, response.getStatus()),
                ()-> assertEquals("{\"birthdate\":\"Birthday must have this format yyyy-MM-dd.\"}", response.getContentAsString())
        );

    }

    @Test
    public void testUpdateClient() throws Exception {
        when(clientService.update(any(Long.class), any(ClientUpdateRequest.class))).thenReturn((clientsResponse.get(0)));

        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                put(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateRequest))
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
    public void testUpdateNotFound() throws Exception {
        when(clientService.update(any(Long.class), any(ClientUpdateRequest.class))).thenThrow(new ClientNotFound(1L));

        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "Client 1",
                "client1",
                0.0,
                "juan@gmail.com",
                "Address 1",
                "123456789",
                LocalDate.now().toString(),
                null,
                false
        );

        MockHttpServletResponse response = mockMvc.perform(
                put(ENDPOINT_URL+"/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertEquals(404, response.getStatus())
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

    @Test
    public void testUpdateImage() throws Exception {

        when(clientService.updateImage(any(Long.class), any(), any())).thenReturn((clientsResponse.get(0)));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "prueba.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "ALGO".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                        multipart(ENDPOINT_URL+"/1/image")
                                .file(file)
                                .with(req -> {
                                    req.setMethod("PATCH");
                                    return req;
                                })
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        ClientResponse res = mapper.readValue(response.getContentAsString(), ClientResponse.class);

        assertAll(
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(clientsResponse.get(0).id(), res.id()),
                ()-> assertEquals(clientsResponse.get(0).name(), res.name()),
                ()-> assertEquals(clientsResponse.get(0).address(), res.address())
        );

        verify(clientService, times(1)).updateImage(any(Long.class), any(), any());

    }


}
