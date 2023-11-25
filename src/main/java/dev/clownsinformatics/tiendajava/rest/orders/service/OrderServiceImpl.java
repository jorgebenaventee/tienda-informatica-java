package dev.clownsinformatics.tiendajava.rest.orders.service;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
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
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@CacheConfig(cacheNames = {"orders"})
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ClientService clientService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.clientService = clientService;
    }

    @Override
    public Page<OrderResponseDto> findAll(Pageable pageable) {
        log.info("Find all orders");
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public OrderResponseDto findById(ObjectId id) {
        log.info("Find order by id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFound(id.toHexString()));
        return orderMapper.toOrderResponseDto(order);
    }

    @Override
    public Page<OrderResponseDto> findByUserId(Long idUser, Pageable pageable) {
        log.info("Find order by customer id: {}", idUser);
        return orderRepository.findByIdUser(idUser, pageable).map(orderMapper::toOrderResponseDto);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public OrderResponseDto save(OrderCreateDto order) {
        log.info("Save order: {}", order);
        ClientResponse client = clientService.findById(order.idUser());
        Order orderMapped = orderMapper.toOrder(order, client);
        checkOrder(orderMapped);

        var orderToSave = reserveStockOrder(orderMapped);
        orderToSave.setCreatedAt(LocalDateTime.now());
        orderToSave.setUpdatedAt(LocalDateTime.now());

        return orderMapper.toOrderResponseDto(orderRepository.save(orderToSave));
    }

    @Override
    @Transactional
    @CacheEvict
    public void delete(ObjectId objectId) {
        log.info("Delete order by id: {}", objectId);
        var orderToDelete = orderRepository.findById(objectId)
                .orElseThrow(() -> new OrderNotFound(objectId.toHexString()));
        returnStockOrders(orderToDelete);
        orderRepository.delete(orderToDelete);
    }

    @Override
    @Transactional
    @CachePut(key = "#objectId")
    public OrderResponseDto update(ObjectId objectId, OrderUpdateDto order) {
        log.info("Update order by id: {}", objectId);
        Order orderToUpdate = orderRepository.findById(objectId).orElseThrow(() -> new OrderNotFound(objectId.toHexString()));
        returnStockOrders(orderToUpdate);

        ClientResponse client = clientService.findById(order.idUser());
        Order orderMapped = orderMapper.toOrder(order, orderToUpdate, client);
        checkOrder(orderMapped);

        var orderToSave = reserveStockOrder(orderMapped);
        orderToSave.setUpdatedAt(LocalDateTime.now());

        return orderMapper.toOrderResponseDto(orderRepository.save(orderToSave));

        /*returnStockOrders(orderToUpdate);
        checkOrder(order);
        var orderToSave = reserveStockOrder(order);
        orderToSave.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(orderToSave);*/
    }

    Order reserveStockOrder(Order order) {
        log.info("Reserve stock for order: {}", order);

        if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
            throw new OrderNotItems(order.getId().toHexString());
        }

        order.getOrderLines().forEach(orderLine -> {
            var product = productRepository.findById(orderLine.getIdProduct()).get();
            product.setStock(product.getStock() - orderLine.getQuantity());
            productRepository.save(product);
            orderLine.setTotal(orderLine.getQuantity() * product.getPrice());
        });

        var total = order.getOrderLines().stream()
                .map(orderLine -> orderLine.getQuantity() * orderLine.getPrice())
                .reduce(0.0, Double::sum);

        var totalItems = order.getOrderLines().stream()
                .map(OrderLine::getQuantity)
                .reduce(0, Integer::sum);

        order.setTotal(total);
        order.setTotalItems(totalItems);

        return order;
    }

    void checkOrder(Order order) {
        log.info("Check order: {}", order);

        if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
            throw new OrderNotItems(order.getId().toHexString());
        }
        order.getOrderLines().forEach(orderLine -> {
            var product = productRepository.findById(orderLine.getIdProduct())
                    .orElseThrow(() -> new ProductNotFound("Product not found with id: " + orderLine.getIdProduct()));
            if (product.getStock() < orderLine.getQuantity() && orderLine.getQuantity() > 0) {
                throw new ProductNotStock("Product stock is: " + product.getStock() + " and order quantity is: " + orderLine.getQuantity());
            }
            if (!product.getPrice().equals(orderLine.getPrice())) {
                throw new ProductBadPrice("Product price is: " + product.getPrice() + " and order price is: " + orderLine.getPrice());
            }
        });
    }

    Order returnStockOrders(Order order) {
        log.info("Return stock for order: {}", order);
        if (order.getOrderLines() != null) {
            order.getOrderLines().forEach(orderLine -> {
                var product = productRepository.findById(orderLine.getIdProduct()).get();
                product.setStock(product.getStock() + orderLine.getQuantity());
                productRepository.save(product);
            });
        }
        return order;
    }
}
