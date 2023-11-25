package dev.clownsinformatics.tiendajava.rest.orders.service;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientService;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.exceptions.OrderNotFound;
import dev.clownsinformatics.tiendajava.rest.orders.exceptions.OrderNotItems;
import dev.clownsinformatics.tiendajava.rest.orders.mappers.OrderMapper;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import dev.clownsinformatics.tiendajava.rest.orders.repository.OrderRepository;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadPrice;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotStock;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository ordersRepository;
    @Mock
    private ProductRepository productsRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ClientService clientService;
    @InjectMocks
    private OrderServiceImpl ordersService;
    
    final UUID idProduct = UUID.randomUUID();
    private final Order order = new Order(new ObjectId("6536518de9b0d305f193b5ef"), 1L, null, List.of(new OrderLine()), 1, 10.0, null, null, false);
    private final Order order2 = new Order(new ObjectId("6536518de9b0d305f193b5ee"), 2L, null, List.of(new OrderLine()), 2, 15.0, null, null, false);
    private final OrderResponseDto orderResponseDto = new OrderResponseDto(new ObjectId("6536518de9b0d305f193b5ef"), 1L, null, List.of(new OrderLine()), 1, 10.0, null, null, false);
    private final OrderResponseDto orderResponseDto2 = new OrderResponseDto(new ObjectId("6536518de9b0d305f193b5ee"), 2L, null, List.of(new OrderLine()), 2, 15.0, null, null, false);

    @Test
    void findAll() {
        List<Order> orders = List.of(order, order2);
        Page<Order> expectedPage = new PageImpl<>(orders);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(ordersRepository.findAll(pageable)).thenReturn(expectedPage);
        when(orderMapper.toOrderResponseDto(order)).thenReturn(orderResponseDto);
        when(orderMapper.toOrderResponseDto(order2)).thenReturn(orderResponseDto2);

        Page<OrderResponseDto> result = ordersService.findAll(pageable);
        
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(orderResponseDto, result.getContent().get(0)),
                () -> assertEquals(orderResponseDto2, result.getContent().get(1))
        );
        
        verify(ordersRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
        ObjectId idOrder = new ObjectId();

        when(ordersRepository.findById(idOrder)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponseDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto resultOrder = ordersService.findById(idOrder);

        assertAll(
                () -> assertEquals(orderResponseDto, resultOrder),
                () -> assertEquals(orderResponseDto.orderLines(), resultOrder.orderLines()),
                () -> assertEquals(orderResponseDto.orderLines().size(), resultOrder.orderLines().size())
        );

        verify(ordersRepository).findById(idOrder);
    }

    @Test
    void testFindByIdNotFound() {
        ObjectId idOrder = new ObjectId();
        
        when(ordersRepository.findById(idOrder)).thenReturn(Optional.empty());
        
        assertThrows(OrderNotFound.class, () -> ordersService.findById(idOrder));
        
        verify(ordersRepository).findById(idOrder);
    }

    @Test
    void testFindByIdUser() {
        Long idUser = 1L;
        List<Order> orders = List.of(order, order2);
        Page<Order> expectedPage = new PageImpl<>(orders);
        Pageable pageable = PageRequest.of(0, 10);

        when(ordersRepository.findByIdUser(idUser, pageable)).thenReturn(expectedPage);
        when(orderMapper.toOrderResponseDto(order)).thenReturn(orderResponseDto);
        when(orderMapper.toOrderResponseDto(order2)).thenReturn(orderResponseDto2);

        Page<OrderResponseDto> result = ordersService.findByUserId(idUser, pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(orderResponseDto, result.getContent().get(0)),
                () -> assertEquals(orderResponseDto2, result.getContent().get(1))
        );

        verify(ordersRepository, times(1)).findByIdUser(idUser, pageable);
    }

    @Test
    void testSave() {
        UUID idProduct = UUID.randomUUID();
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        OrderLine lineaOrder = OrderLine.builder().idProduct(product.getId()).quantity(2).price(10.0).total(20.0).build();
        OrderCreateDto orderDto = new OrderCreateDto(1L, List.of(lineaOrder));

        Order orderToSave = new Order();
        orderToSave.setIdUser(orderDto.idUser());
        orderToSave.setOrderLines(List.of(lineaOrder));

        when(productsRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(ordersRepository.save(any(Order.class))).thenReturn(orderToSave);
        when(orderMapper.toOrder(any(OrderCreateDto.class), any())).thenReturn(orderToSave);
        when(orderMapper.toOrderResponseDto(any(Order.class))).thenReturn(orderResponseDto);

        OrderResponseDto resultOrder = ordersService.save(orderDto);

        assertAll(
                () -> assertEquals(orderResponseDto, resultOrder),
                () -> assertEquals(orderResponseDto.orderLines(), resultOrder.orderLines()),
                () -> assertEquals(orderResponseDto.orderLines().size(), resultOrder.orderLines().size())
        );

        verify(ordersRepository).save(any(Order.class));
        verify(productsRepository, times(2)).findById(any(UUID.class));
    }

    @Test
    void testSaveNotItems() {
        ClientResponse client = new ClientResponse(1L, "name", "surname", 0.0, "email", "address", "phone", LocalDate.now(), "image", false, null, null);
        OrderCreateDto order = new OrderCreateDto(1L, null);

        when(clientService.findById(any(Long.class))).thenReturn(client);
        when(orderMapper.toOrder(any(OrderCreateDto.class), any())).thenReturn(Order.builder().orderLines(null).build());

        assertThrows(OrderNotItems.class, () -> ordersService.save(order));

        verify(clientService).findById(any(Long.class));
        verify(orderMapper).toOrder(any(OrderCreateDto.class), any());
    }

    @Test
    void testDelete() {
        ObjectId idOrder = new ObjectId();
        Order orderToDelete = new Order();
        
        when(ordersRepository.findById(idOrder)).thenReturn(Optional.of(orderToDelete));
        ordersService.delete(idOrder);
        
        verify(ordersRepository).findById(idOrder);
        verify(ordersRepository).delete(orderToDelete);
    }

    @Test
    void testDeleteNotFound() {
        ObjectId idOrder = new ObjectId();
        
        when(ordersRepository.findById(idOrder)).thenReturn(Optional.empty());
        
        assertThrows(OrderNotFound.class, () -> ordersService.delete(idOrder));
        
        verify(ordersRepository).findById(idOrder);
        verify(ordersRepository, never()).deleteById(idOrder);
    }

    @Test
    void testUpdate() {
        UUID idProduct = UUID.randomUUID();
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        OrderLine lineaOrder = OrderLine.builder().idProduct(product.getId()).quantity(2).price(10.0).total(20.0).build();
        OrderUpdateDto orderDto = new OrderUpdateDto(1L, List.of(lineaOrder));

        Order orderToUpdate = new Order();
        orderToUpdate.setIdUser(orderDto.idUser());
        orderToUpdate.setOrderLines(List.of(lineaOrder));

        when(productsRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(ordersRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(orderToUpdate));
        when(ordersRepository.save(any(Order.class))).thenReturn(orderToUpdate);
        when(orderMapper.toOrder(any(OrderUpdateDto.class), any(), any())).thenReturn(orderToUpdate);
        when(orderMapper.toOrderResponseDto(any(Order.class))).thenReturn(orderResponseDto);

        OrderResponseDto resultOrder = ordersService.update(new ObjectId(), orderDto);

        assertAll(
                () -> assertEquals(orderResponseDto, resultOrder),
                () -> assertEquals(orderResponseDto.orderLines(), resultOrder.orderLines()),
                () -> assertEquals(orderResponseDto.orderLines().size(), resultOrder.orderLines().size())
        );

        verify(ordersRepository).save(any(Order.class));
        verify(productsRepository, times(3)).findById(any(UUID.class));
        verify(ordersRepository).findById(any(ObjectId.class));
    }

    @Test
    void testUpdateNotFound() {
        UUID idProduct = UUID.randomUUID();
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        OrderLine lineaOrder = OrderLine.builder().idProduct(product.getId()).quantity(2).price(10.0).total(20.0).build();
        OrderUpdateDto orderDto = new OrderUpdateDto(1L, List.of(lineaOrder));

        when(ordersRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(OrderNotFound.class, () -> ordersService.update(new ObjectId(), orderDto));

        verify(ordersRepository).findById(any(ObjectId.class));
    }

    @Test
    void testReserveStockOrder() throws OrderNotFound {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineasOrder.add(lineaOrder);
        order.setOrderLines(lineasOrder);
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.of(product));
        
        Order result = ordersService.reserveStockOrder(order);
        
        assertAll(() -> assertEquals(3, product.getStock()),
                () -> assertEquals(20.0, lineaOrder.getTotal()),
                () -> assertEquals(20.0, result.getTotal()),
                () -> assertEquals(2, result.getTotalItems())
        );
        
        verify(productsRepository, times(1)).findById(idProduct);
        verify(productsRepository, times(1)).save(product);
    }

    @Test
    void returnStockOrderWithUpdateStock() {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder1 = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineasOrder.add(lineaOrder1);
        order.setOrderLines(lineasOrder);
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(13).img("https://placehold.co/600x400").category(null).build();
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.of(product));
        when(productsRepository.save(product)).thenReturn(product);
        
        Order result = ordersService.returnStockOrders(order);
        
        assertEquals(15, product.getStock());
        assertEquals(order, result);
        
        verify(productsRepository, times(1)).findById(idProduct);
        verify(productsRepository, times(1)).save(product);
    }

    @Test
    void checkOrder_ProductExistenYHayStock() {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder1 = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineasOrder.add(lineaOrder1);
        order.setOrderLines(lineasOrder);
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.of(product));
        
        assertDoesNotThrow(() -> ordersService.checkOrder(order));
        
        verify(productsRepository, times(1)).findById(idProduct);
    }

    @Test
    void checkOrderNotFound() {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder1 = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineasOrder.add(lineaOrder1);
        order.setOrderLines(lineasOrder);
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.empty());
        
        assertThrows(ProductNotFound.class, () -> ordersService.checkOrder(order));
        
        verify(productsRepository, times(1)).findById(idProduct);
    }

    @Test
    void checkOrderNotEnoughtStock() {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder1 = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineaOrder1.setIdProduct(idProduct);
        lineaOrder1.setQuantity(10);
        lineasOrder.add(lineaOrder1);
        order.setOrderLines(lineasOrder);
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.of(product));
        
        assertThrows(ProductNotStock.class, () -> ordersService.checkOrder(order));
        
        verify(productsRepository, times(1)).findById(idProduct);
    }

    @Test
    void checkOrderBadPrice() {
        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        OrderLine lineaOrder1 = OrderLine.builder().idProduct(idProduct).quantity(2).price(10.0).total(20.0).build();
        lineaOrder1.setIdProduct(idProduct);
        lineaOrder1.setQuantity(2);
        lineaOrder1.setPrice(20.0);
        lineasOrder.add(lineaOrder1);
        order.setOrderLines(lineasOrder);
        Product product = Product.builder().id(idProduct).name("Product 1").price(10.0).stock(5).img("https://placehold.co/600x400").category(null).build();
        
        when(productsRepository.findById(idProduct)).thenReturn(Optional.of(product));
        
        assertThrows(ProductBadPrice.class, () -> ordersService.checkOrder(order));
        
        verify(productsRepository, times(1)).findById(idProduct);
    }
}