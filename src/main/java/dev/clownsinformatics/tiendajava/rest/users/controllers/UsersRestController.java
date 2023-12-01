package dev.clownsinformatics.tiendajava.rest.users.controllers;

import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import dev.clownsinformatics.tiendajava.rest.orders.service.OrderService;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserInfoResponse;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserRequest;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserResponse;
import dev.clownsinformatics.tiendajava.rest.users.exceptions.UnauthorizedUser;
import dev.clownsinformatics.tiendajava.rest.users.models.User;
import dev.clownsinformatics.tiendajava.rest.users.services.UsersService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("api/users")
@PreAuthorize("hasRole('USER')")
public class UsersRestController {
    private final UsersService usersService;
    private final PaginationLinksUtils paginationLinksUtils;
    private final OrderService orderService;

    @Autowired
    public UsersRestController(UsersService usersService, PaginationLinksUtils paginationLinksUtils, OrderService orderService) {
        this.usersService = usersService;
        this.paginationLinksUtils = paginationLinksUtils;
        this.orderService = orderService;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username  username del usuario
     * @param email     email del usuario
     * @param isDeleted si está borrado o no
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @param request   petición
     * @return Respuesta con la página de usuarios
     */

    @Operation(summary = "Find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "username", required = false, description = "Username"),
            @Parameter(name = "email", required = false, description = "Email"),
            @Parameter(name = "isDeleted", required = false, description = "Is deleted"),
            @Parameter(name = "page", required = false, description = "Page"),
            @Parameter(name = "size", required = false, description = "Size"),
            @Parameter(name = "sortBy", required = false, description = "Sort by"),
            @Parameter(name = "direction", required = false, description = "Direction"),
            @Parameter(name = "request", required = true, description = "Request")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id del usuario, se pasa como parámetro de la URL /{id}
     * @return Usuario si existe
     * @throws dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound si no existe el usuario (404)
     */
    @Operation(summary = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "User id")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un nuevo usuario
     *
     * @param userRequest usuario a crear
     * @return Usuario creado
     * @throws dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNameOrEmailExists si el nombre de usuario o el email ya existen
     * @throws HttpClientErrorException.BadRequest                                          si hay algún error de validación
     */
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "userRequest", required = true, description = "User data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          id del usuario
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound          si no existe el usuario (404)
     * @throws HttpClientErrorException.BadRequest                                          si hay algún error de validación (400)
     * @throws dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNameOrEmailExists si el nombre de usuario o el email ya existen (400)
     */
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "User id"),
            @Parameter(name = "userRequest", required = true, description = "User data")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id id del usuario
     * @return Respuesta vacía
     * @throws dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound si no existe el usuario (404)
     */
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "User id")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     */
    @Operation(summary = "Get current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data")
    })
    @GetMapping("/me/profile")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    /**
     * Actualiza el usuario actual
     *
     * @param user        usuario autenticado
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     */
    @Operation(summary = "Update current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "userRequest", required = true, description = "User data")
    })
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user, @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    /**
     * Borra el usuario actual
     *
     * @param user usuario autenticado
     * @return Respuesta vacía
     */
    @Operation(summary = "Delete current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data")
    })
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los pedidos del usuario actual
     *
     * @param user      usuario autenticado
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @return Respuesta con la página de pedidos
     */
    @Operation(summary = "Get current user orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "page", required = false, description = "Page"),
            @Parameter(name = "size", required = false, description = "Size"),
            @Parameter(name = "sortBy", required = false, description = "Sort by"),
            @Parameter(name = "direction", required = false, description = "Direction")
    })
    @GetMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<OrderResponseDto>> getPedidosByUsuario(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo pedidos del usuario con id: " + user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(orderService.findByUserId(user.getId(), pageable), sortBy, direction));
    }


    /**
     * Obtiene un pedido del usuario actual
     *
     * @param user     usuario autenticado
     * @param idPedido id del pedido
     * @return Pedido
     */
    @Operation(summary = "Get current user order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "idPedido", required = true, description = "Order id")
    })
    @GetMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDto> getPedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        log.info("Obteniendo pedido con id: " + idPedido);
        if (!orderService.findById(idPedido).idUser().equals(user.getId())) {
            throw new UnauthorizedUser("The user is not authorized to get this order");
        }
        return ResponseEntity.ok(orderService.findById(idPedido));
    }


    /**
     * Crea un pedido para el usuario actual
     *
     * @param user   usuario autenticado
     * @param pedido pedido a crear
     * @return Pedido creado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws HttpClientErrorException.Unauthorized si el usuario no está autorizado (403)
     */
    @Operation(summary = "Create current user order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "pedido", required = true, description = "Order data")
    })
    @PostMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDto> savePedido(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderCreateDto pedido
    ) {
        log.info("Creando pedido: " + pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(pedido));
    }


    /**
     * Actualiza un pedido del usuario actual
     *
     * @param user     usuario autenticado
     * @param idPedido id del pedido
     * @param pedido   pedido a actualizar
     * @return Pedido actualizado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws HttpClientErrorException.Unauthorized si el usuario no está autorizado (403)
     */
    @Operation(summary = "Update current user order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "idPedido", required = true, description = "Order id"),
            @Parameter(name = "pedido", required = true, description = "Order data")
    })
    @PutMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDto> updatePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido,
            @Valid @RequestBody OrderUpdateDto pedido) {
        log.info("Actualizando pedido con id: " + idPedido);
        OrderResponseDto order = orderService.findById(idPedido);
        if (!order.idUser().equals(user.getId())) {
            throw new UnauthorizedUser("The user is not authorized to update this order");
        }
        return ResponseEntity.ok(orderService.update(idPedido, pedido));
    }


    /**
     * Borra un pedido del usuario actual
     *
     * @param user     usuario autenticado
     * @param idPedido id del pedido
     * @return Pedido borrado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws HttpClientErrorException.Unauthorized si el usuario no está autorizado (403)
     */
    @Operation(summary = "Delete current user order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "user", required = true, description = "User data"),
            @Parameter(name = "idPedido", required = true, description = "Order id")
    })
    @DeleteMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deletePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        log.info("Borrando pedido con id: " + idPedido);
        OrderResponseDto order = orderService.findById(idPedido);
        if (!order.idUser().equals(user.getId())) {
            throw new UnauthorizedUser("The user is not authorized to delete this order");
        }
        orderService.delete(idPedido);
        return ResponseEntity.noContent().build();
    }


    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
