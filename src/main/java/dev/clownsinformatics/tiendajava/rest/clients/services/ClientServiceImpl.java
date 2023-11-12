package dev.clownsinformatics.tiendajava.rest.clients.services;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.exceptions.ClientNotFound;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.FileSystemStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = "clients")
@Slf4j
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final FileSystemStorageService fileSystemStorageService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, FileSystemStorageService fileSystemStorageService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.fileSystemStorageService = fileSystemStorageService;
    }


    @Override
    public Page<ClientResponse> findAll(Optional<String> username, Optional<Boolean> isDeleted, Pageable pageable) {

        Specification<Client> specUsername = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + username.orElse("") + "%");

        Specification<Client> specIsDeleted = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), isDeleted.orElse(false));

        Specification<Client> specs = Specification.where(specUsername).and(specIsDeleted);

        Page<ClientResponse> response = clientRepository.findAll(specs, pageable).map(clientMapper::toClientResponse);

        return response;

    }

    @Override
    @Cacheable(key = "#id")
    public ClientResponse findById(Long id) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ClientNotFound(id)
        ));
        return response;
    }

    @Override
    public ClientResponse findByName(String name) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.findByNameAndIsDeletedFalse(name));
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
        findById(id);
        String imageUrl = fileSystemStorageService.store(image);

        imageUrl = withUrl ? fileSystemStorageService.getUrl(imageUrl) : imageUrl;

        ClientResponse response = clientMapper.toClientResponse(clientRepository.updateImageById(id, imageUrl));

        return response;
    }
}
