package dev.clownsinformatics.tiendajava.rest.clients.controllers;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("${api.version}/clientes")
@Slf4j
public class ClientRestController {


    private final ClientService clientService;
    private final PaginationLinksUtils paginationLinksUtils;


    @Autowired
    public ClientRestController(ClientService clientService, PaginationLinksUtils paginationLinksUtils) {
        this.clientService = clientService;
        this.paginationLinksUtils = paginationLinksUtils;
    }


    @PostMapping("/")
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientCreateRequest clientCreateRequest) {
        log.info("Creating client");
        return ResponseEntity.ok(clientService.save(clientCreateRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody @Valid ClientUpdateRequest clientUpdateRequest) {
        log.info("Updating client");
        return ResponseEntity.ok(clientService.update(id, clientUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {
        log.info("Getting client");
        return ResponseEntity.ok(clientService.findById(id));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Deleting client");
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
