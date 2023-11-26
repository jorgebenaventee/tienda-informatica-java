package dev.clownsinformatics.tiendajava.rest.orders.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.exceptions.OrderNotFound;
import dev.clownsinformatics.tiendajava.rest.orders.exceptions.OrderNotItems;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import dev.clownsinformatics.tiendajava.rest.orders.service.OrderService;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadPrice;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotStock;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
class OrderRestControllerTest {
    private final String myEndpoint = "/api/pedidos";
    private final ObjectMapper mapper = new ObjectMapper();

    private final ClientResponse clientResponse = new ClientResponse(1L, "Juan", "Perez", 0.0, "email", "address", "phone", LocalDate.now(), "image", false, LocalDateTime.now(), LocalDateTime.now());
    private final OrderLine orderLine = OrderLine.builder().idProduct(UUID.randomUUID()).quantity(2).price(10.0).build();
    private final Order order1 = Order.builder().id(new ObjectId("5f9f1a3b9d6b6d2e3c1d6f1a")).idUser(1L).client(clientResponse).orderLines(List.of(orderLine)).build();
    private final OrderResponseDto orderResponseDto = new OrderResponseDto(order1.getId().toHexString(), order1.getIdUser(), order1.getClient(), order1.getOrderLines(), order1.getTotalItems(), order1.getTotal(), order1.getCreatedAt(), order1.getUpdatedAt(), order1.getIsDeleted());

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Autowired
    public OrderRestControllerTest(OrderService orderService) {
        this.orderService = orderService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllOrders() throws Exception {
        List<OrderResponseDto> ordersList = List.of(orderResponseDto);
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        PageImpl<OrderResponseDto> page = new PageImpl<>(ordersList);

        when(orderService.findAll(pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<OrderResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );


        verify(orderService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getOrderById() throws Exception {

        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        when(orderService.findById(any(ObjectId.class))).thenReturn(orderResponseDto);


        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        OrderResponseDto res = mapper.readValue(response.getContentAsString(), OrderResponseDto.class);


        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(order1.get_id(), res.id())
        );


        verify(orderService, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void getOrderByIdNoFound() throws Exception {

        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        when(orderService.findById(any(ObjectId.class)))
                .thenThrow(new OrderNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));


        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(404, response.getStatus())
        );


        verify(orderService, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void getOrdersByUsuario() throws Exception {

        var myLocalEndpoint = myEndpoint + "/user/1";
        var ordersList = List.of(orderResponseDto);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(ordersList);


        when(orderService.findByUserId(anyLong(), any(Pageable.class))).thenReturn(page);


        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<OrderResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );


        verify(orderService, times(1)).findByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    void createOrder() throws Exception {

        when(orderService.save(any(OrderCreateDto.class))).thenReturn(orderResponseDto);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();

        OrderResponseDto res = mapper.readValue(response.getContentAsString(), OrderResponseDto.class);


        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(order1.get_id(), res.id())
        );


        verify(orderService, times(1)).save(any(OrderCreateDto.class));
    }

    @Test
    void createOrderNoItemsBadRequest() throws Exception {

        when(orderService.save(any(OrderCreateDto.class))).thenThrow(new OrderNotItems("5f9f1a3b9d6b6d2e3c1d6f1a"));


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(400, response.getStatus())
        );


        verify(orderService).save(any(OrderCreateDto.class));
    }

    @Test
    void createOrderProductBadPriceBadRequest() throws Exception {

        when(orderService.save(any(OrderCreateDto.class))).thenThrow(new ProductBadPrice("Bad price"));


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(400, response.getStatus())
        );


        verify(orderService).save(any(OrderCreateDto.class));
    }

    @Test
    void getOrdersProductNotFound() throws Exception {
        when(orderService.save(any(OrderCreateDto.class))).thenThrow(new ProductNotFound("Product not found"));


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(404, response.getStatus())
        );


        verify(orderService).save(any(OrderCreateDto.class));
    }

    @Test
    void getOrdersProductNotStockBadRequest() throws Exception {

        when(orderService.save(any(OrderCreateDto.class))).thenThrow(new ProductNotStock("Product not stock"));


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(400, response.getStatus())
        );


        verify(orderService).save(any(OrderCreateDto.class));
    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        when(orderService.update(any(ObjectId.class), any(OrderUpdateDto.class))).thenReturn(orderResponseDto);


        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();

        OrderResponseDto res = mapper.readValue(response.getContentAsString(), OrderResponseDto.class);


        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(order1.get_id(), res.id())
        );


        verify(orderService, times(1)).update(any(ObjectId.class), any(OrderUpdateDto.class));
    }

    @Test
    void updateOrderNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        when(orderService.update(any(ObjectId.class), any(OrderUpdateDto.class)))
                .thenThrow(new OrderNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));


        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                                .content(mapper.writeValueAsString(order1)))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(404, response.getStatus())
        );


        verify(orderService, times(1)).update(any(ObjectId.class), any(OrderUpdateDto.class));
    }

    // Habría que testear casi lo mismo en el save con el update

    @Test
    void deleteOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        doNothing().when(orderService).delete(any(ObjectId.class));


        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(204, response.getStatus())
        );


        verify(orderService, times(1)).delete(any(ObjectId.class));
    }

    @Test
    void deleteOrderNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";


        doThrow(new OrderNotFound("5f9f1a3b9d6b6d2e3c1d6f1a")).when(orderService).delete(any(ObjectId.class));


        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(404, response.getStatus())
        );


        verify(orderService, times(1)).delete(any(ObjectId.class));
    }
}