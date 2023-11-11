package dev.clownsinformatics.tiendajava.rest.clients.controllers;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.services.ClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/clientes")
@Slf4j
public class ClientRestController {


    private final ClientService clientService;


    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Deleting client");
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
