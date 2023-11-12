package dev.clownsinformatics.tiendajava.rest.clients.services;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ClientService {


    Page<ClientResponse> findAll(Optional<String> username, Optional<Boolean> isDeleted, Pageable pageable);

    ClientResponse findById(Long id);

    ClientResponse findByName(String name);

    ClientResponse save(ClientCreateRequest productoCreateRequest);

    ClientResponse update(Long id, ClientUpdateRequest productoUpdateRequest);

    void deleteById(Long id);

    ClientResponse updateImage(Long id, MultipartFile image, Boolean withUrl);



}
