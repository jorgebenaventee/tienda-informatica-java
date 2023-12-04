package dev.clownsinformatics.tiendajava.rest.orders.controller;

import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.service.OrderService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class OrderRestController {
    private final OrderService orderService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public OrderRestController(OrderService orderService, PaginationLinksUtils paginationLinksUtils) {
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los pedidos paginados y proporciona enlaces de paginación.
     *
     * @param page      Número de página.
     * @param size      Número de elementos por página.
     * @param sortBy    Campo por el cual se ordenan los resultados.
     * @param direction Dirección de ordenamiento (ASC o DESC).
     * @param request   Objeto HttpServletRequest para construir enlaces de paginación.
     * @return ResponseEntity con la lista de pedidos paginada y enlaces de paginación.
     */
    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all orders"),
            @ApiResponse(responseCode = "400", description = "Invalid page size supplied"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @Parameters({
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Number of elements per page", example = "10"),
            @Parameter(name = "sortBy", description = "Default sort order is ascending.", example = "id"),
            @Parameter(name = "direction", description = "Sorting order in the format: ASC|DESC. Default sort order is ascending.", example = "asc")
    })
    @GetMapping
    public ResponseEntity<PageResponse<OrderResponseDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Getting all orders");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<OrderResponseDto> pageResult = orderService.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un pedido por su identificador.
     *
     * @param id Identificador del pedido.
     * @return ResponseEntity con el pedido encontrado.
     */
    @Operation(summary = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order"),
            @ApiResponse(responseCode = "400", description = "Invalid order id supplied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "Order id", example = "60f0a9b9e4b0e3a6f0f3f0a1", required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") ObjectId id) {
        log.info("Getting order with id {}", id);
        return ResponseEntity.ok(orderService.findById(id));
    }

    /**
     * Obtiene pedidos de un usuario paginados y proporciona enlaces de paginación.
     *
     * @param id        Identificador del usuario.
     * @param page      Número de página.
     * @param size      Número de elementos por página.
     * @param sortBy    Campo por el cual se ordenan los resultados.
     * @param direction Dirección de ordenamiento (ASC o DESC).
     * @param request   Objeto HttpServletRequest para construir enlaces de paginación.
     * @return ResponseEntity con la lista de pedidos del usuario paginada y enlaces de paginación.
     */
    @Operation(summary = "Get orders by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the orders"),
            @ApiResponse(responseCode = "400", description = "Invalid user id supplied"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "User id", example = "60f0a9b9e4b0e3a6f0f3f0a1", required = true),
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Number of elements per page", example = "10"),
            @Parameter(name = "sortBy", description = "Default sort order is ascending.", example = "id"),
            @Parameter(name = "direction", description = "Default sort order is ascending.", example = "asc")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<PageResponse<OrderResponseDto>> getOrdersByUserId(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Getting orders by user id {}", id);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(orderService.findByUserId(id, pageable), sortBy, direction));
    }

    /**
     * Crea un nuevo pedido.
     *
     * @param order Datos del nuevo pedido.
     * @return ResponseEntity con el pedido creado.
     */
    @Operation(summary = "Get orders by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the orders"),
            @ApiResponse(responseCode = "400", description = "Invalid status supplied"),
            @ApiResponse(responseCode = "404", description = "Orders not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "order create body", description = "Order create body", required = true)
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateDto order) {
        log.info("Creating order {}", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    /**
     * Actualiza un pedido existente por su identificador.
     *
     * @param id    Identificador del pedido a actualizar.
     * @param order Datos actualizados del pedido.
     * @return ResponseEntity con el pedido actualizado.
     */
    @Operation(summary = "Update order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the order"),
            @ApiResponse(responseCode = "400", description = "Invalid order id supplied"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "id", description = "Order id", example = "60f0a9b9e4b0e3a6f0f3f0a1", required = true),
            @Parameter(name = "order update body", description = "Order update body", required = true)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("id") ObjectId id, @Valid @RequestBody OrderUpdateDto order) {
        log.info("Updating order with id {}", id);
        return ResponseEntity.ok(orderService.update(id, order));
    }

    /**
     * Elimina un pedido por su identificador.
     *
     * @param id Identificador del pedido a eliminar.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Delete order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the order"),
            @ApiResponse(responseCode = "400", description = "Invalid order id supplied"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "id", description = "Order id", example = "60f0a9b9e4b0e3a6f0f3f0a1", required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") ObjectId id) {
        log.info("Deleting order with id {}", id);
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja excepciones de validación para devolver mensajes de error estructurados.
     *
     * @param ex Excepción de validación.
     * @return Mapa de errores con nombres de campo y mensajes asociados.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}