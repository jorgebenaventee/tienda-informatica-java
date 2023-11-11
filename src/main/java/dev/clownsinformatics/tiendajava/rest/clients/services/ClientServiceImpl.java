package dev.clownsinformatics.tiendajava.rest.clients.services;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = "clients")
@Slf4j
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }


    @Override
    public Page<ClientResponse> findAll(Optional<String> username, Optional<Boolean> isDeleted, Pageable pageable) {
        return null;
    }

    @Override
    @Cacheable(key = "#id")
    public ClientResponse findById(Long id) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.findById(id).orElse(null));
        return response;
    }

    @Override
    public ClientResponse findByName(String name) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.findByName(name));
        return response;
    }

    @Override
    @CachePut(key = "#result.id")
    public ClientResponse save(ClientCreateRequest productoCreateRequest) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.save(clientMapper.toClient(productoCreateRequest)));
        return response;
    }

    @Override
    @CachePut(key = "#result.id")
    public ClientResponse update(Long id, ClientUpdateRequest productoUpdateRequest) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.save(clientMapper.toClient(productoUpdateRequest)));
        return response;
    }

    @Override
    @CachePut(key = "#id")
    public void deleteById(Long id) {
        findById(id);
        clientRepository.deleteById(id);
    }

    @Override
    @CachePut(key = "#result.id")
    public ClientResponse updateImage(Long id, MultipartFile image, Boolean withUrl) {
        return null;
    }
}
