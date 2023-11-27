package dev.clownsinformatics.tiendajava.rest.clients.controllers;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientServiceImpl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/clients")
@Slf4j
public class ClientRestController {


    private final ClientServiceImpl clientService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public ClientRestController(ClientServiceImpl clientService, PaginationLinksUtils paginationLinksUtils) {
        this.clientService = clientService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @Operation(summary = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Client already exists")
    })
    @Parameters({
            @Parameter(name = "clientCreateRequest", required = true, description = "Client to create")
    })
    @PostMapping("/")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreateRequest clientCreateRequest) {
        log.info("Creating client");
        ClientResponse clientResponse = clientService.save(clientCreateRequest);
        return ResponseEntity.ok(clientResponse);
    }

    @Operation(summary = "Update a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "Client id"),
            @Parameter(name = "clientUpdateRequest", required = true, description = "Client to update")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequest clientUpdateRequest) {
        log.info("Updating client");
        return ResponseEntity.ok(clientService.update(id, clientUpdateRequest));
    }

    @Operation(summary = "Get a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "Client id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {
        log.info("Getting client");
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(summary = "Get all clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients found")
    })
    @Parameters({
            @Parameter(name = "username", description = "Client username"),
            @Parameter(name = "isDeleted", description = "Client is deleted"),
            @Parameter(name = "page", description = "Page number"),
            @Parameter(name = "size", description = "Page size"),
            @Parameter(name = "sortBy", description = "Sort by"),
            @Parameter(name = "direction", description = "Sort direction")
    })
    @GetMapping("/")
    public ResponseEntity<PageResponse<ClientResponse>> getAllClients(@RequestParam(required = false) Optional<String> username, @RequestParam(defaultValue = "false") String isDeleted,
                                                                      @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sortBy,
                                                                      @RequestParam(defaultValue = "asc") String direction, HttpServletRequest request) {
        log.info("Getting all clients");

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ClientResponse> pageResult = clientService.findAll(username, Optional.of(Boolean.valueOf(isDeleted)), PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));

    }

    @Operation(summary = "Delete a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "Client id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Deleting client");
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a client image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client image updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @Parameters({
            @Parameter(name = "id", required = true, description = "Client id"),
            @Parameter(name = "file", required = true, description = "Client image")
    })
    @PatchMapping("/{id}/image")
    public ResponseEntity<ClientResponse> updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file, @RequestParam("withUrl") Optional<Boolean> withUrl) {
        log.info("Updating image");
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clientService.updateImage(id, file, withUrl.orElse(true)));
    }

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
