package dev.clownsinformatics.tiendajava.rest.users.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import dev.clownsinformatics.tiendajava.rest.orders.service.OrderService;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserInfoResponse;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserRequest;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserResponse;
import dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound;
import dev.clownsinformatics.tiendajava.rest.users.models.User;
import dev.clownsinformatics.tiendajava.rest.users.services.UsersService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class UsersRestControllerTest {
    UUID uuid = UUID.randomUUID();
    LocalDate localDate = LocalDate.now();
    LocalDateTime localDateTime = LocalDateTime.now();

    private final UserRequest userRequest = UserRequest.builder()
            .name("test")
            .lastName("test")
            .password("test1234")
            .username("test")
            .email("test@test.com")
            .build();
    private final User user = User.builder()
            .id(99L)
            .name("test")
            .lastName("test")
            .password("test1234")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserResponse userResponse = UserResponse.builder()
            .id(99L)
            .name("test")
            .lastName("test")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
            .id(99L)
            .name("test")
            .lastName("test")
            .username("test")
            .email("test@test.com")
            .build();
    private final String BASE_URL = "/api/users";
    private final ObjectMapper mapper = new ObjectMapper();
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


    OrderLine orderLine = OrderLine.builder()
            .idProduct(uuid)
            .price(99.99)
            .total(99.99)
            .build();
    private final Order order = Order.builder()
            .id(new ObjectId("5f9c7b9b9c6b9e1d7c9d9c9d"))
            .client(clientsResponse.get(0))
            .orderLines(List.of(orderLine))
            .idUser(2L)
            .build();

    private final OrderResponseDto orderResponseDto = new OrderResponseDto(order.getId().toHexString(), order.getIdUser(), order.getClient(), order.getOrderLines(), order.getTotalItems(), order.getTotal(), order.getCreatedAt(), order.getUpdatedAt(), order.getIsDeleted());

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UsersService usersService;
    @MockBean
    private OrderService orderService;

    @Autowired
    public UsersRestControllerTest(UsersService UsersService) {
        this.usersService = UsersService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithAnonymousUser
    void NotAuthenticated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    void findAll() throws Exception {
        var list = List.of(userResponse);
        Page<UserResponse> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<UserResponse> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        verify(usersService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void findById() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        when(usersService.findById(anyLong())).thenReturn(userInfoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserInfoResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userInfoResponse, res)
        );

        verify(usersService, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        when(usersService.findById(anyLong())).thenThrow(new UserNotFound("No existe el usuario"));
        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(usersService, times(1)).findById(anyLong());
    }

    @Test
    void createUser() throws Exception {

        var LOCAL_URL = BASE_URL;
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );
        verify(usersService, times(1)).save(any(UserRequest.class));
    }

    @Test
    void createUserBadRequestNameNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createUserBadRequestSurnamesNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("")
                .password("test1234")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createUserBadRequestUsernameNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createUserBadRequestEmailInvalid() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("test")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createUserBadRequesEmailNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createUserBadRequestPasswordLenghtMin() throws Exception {

        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test")
                .username("test")
                .email("test@test.com")
                .build();
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
        verify(usersService, times(0)).save(any(UserRequest.class));
    }

    @Test
    void createUserBadRequestPasswordNotEmtpy() throws Exception {
        var LOCAL_URL = BASE_URL;

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUser() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        when(usersService.update(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );
        verify(usersService, times(1)).update(anyLong(), any(UserRequest.class));
    }

    @Test
    void updateUserNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        when(usersService.update(anyLong(), any(UserRequest.class))).thenThrow(new UserNotFound("No existe el usuario"));

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(usersService, times(1)).update(anyLong(), any(UserRequest.class));
    }

    @Test
    void updateUserBadRequestNameNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUserBadRequestSurnamesNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("")
                .password("test1234")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUserBadRequestUsernameNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUserBadRequestEmailInvalid() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("test")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUserBadRequesEmailNotEmpty() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("test1234")
                .username("test")
                .email("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateUserBadRequestPasswordNotEmtpy() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        var userRequest = UserRequest.builder()
                .name("test")
                .lastName("test")
                .password("")
                .username("test")
                .email("test@test.com")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deleteUser() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        doNothing().when(usersService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(204, response.getStatus());

        verify(usersService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";

        doThrow(new UserNotFound("No existe el usuario")).when(usersService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(usersService, times(1)).deleteById(anyLong());
    }

    @Test
    @WithUserDetails("user")
    void me() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/profile";
        when(usersService.findById(anyLong())).thenReturn(userInfoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }


    @Test
    @WithAnonymousUser
    void me_AnonymousUser() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/profile";
        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    @WithUserDetails("user")
    void updateMe() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/profile";

        when(usersService.update(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );
        verify(usersService, times(1)).update(anyLong(), any(UserRequest.class));
    }

    @Test
    @WithUserDetails("user")
    void deleteMe() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/profile";
        doNothing().when(usersService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());

        verify(usersService, times(1)).deleteById(anyLong());
    }

    @Test
    @WithUserDetails("user")
    void getPedidosByUsuario() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/pedidos";
        var page = new PageImpl<>(List.of(orderResponseDto));

        when(orderService.findByUserId(anyLong(), any(Pageable.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(orderService, times(1)).findByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    @WithUserDetails("user")
    void getPedido() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/pedidos/" + order.getId();

        when(orderService.findById(any(ObjectId.class))).thenReturn(orderResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(orderService, times(1)).findById(any(ObjectId.class));
    }

    @Test
    @WithUserDetails("user")
    void savePedido() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/pedidos";

        when(orderService.save(any(OrderCreateDto.class))).thenReturn(orderResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        post(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());

        verify(orderService, times(1)).save(any(OrderCreateDto.class));
    }

    @Test
    @WithUserDetails("user")
    void updatePedido() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/pedidos/" + order.getId();

        when(orderService.update(any(ObjectId.class), any(OrderUpdateDto.class))).thenReturn(orderResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(orderService, times(1)).update(any(ObjectId.class), any(OrderUpdateDto.class));
    }

    @Test
    @WithUserDetails("user")
    void deletePedido() throws Exception {
        var LOCAL_URL = BASE_URL + "/me/pedidos/" + order.getId();
        when(orderService.findById(any(ObjectId.class))).thenReturn(orderResponseDto);
        doNothing().when(orderService).delete(any(ObjectId.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());

        verify(orderService, times(1)).delete(any(ObjectId.class));
    }

}