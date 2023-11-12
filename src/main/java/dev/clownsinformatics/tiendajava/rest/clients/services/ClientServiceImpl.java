package dev.clownsinformatics.tiendajava.rest.clients.services;


import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.exceptions.ClientNotFound;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.FileSystemStorageService;
import jakarta.transaction.Transactional;
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

        ClientResponse currentClient = findById(id);

        Client clientToSave = Client.builder()
                .id(id)
                .name(currentClient.name())
                .username(currentClient.username())
                .email(currentClient.email())
                .address(currentClient.address())
                .phone(currentClient.phone())
                .birthdate(currentClient.birthdate())
                .image(currentClient.image())
                .balance(currentClient.balance())
                .isDeleted(currentClient.isDeleted())
                .build();

        if (productoUpdateRequest.name() != null){
            clientToSave.setName(productoUpdateRequest.name());
        }
        if (productoUpdateRequest.username() != null){
            clientToSave.setUsername(productoUpdateRequest.username());
        }
        if (productoUpdateRequest.email() != null){
            clientToSave.setEmail(productoUpdateRequest.email());
        }
        if (productoUpdateRequest.address() != null){
            clientToSave.setAddress(productoUpdateRequest.address());
        }
        if (productoUpdateRequest.phone() != null){
            clientToSave.setPhone(productoUpdateRequest.phone());
        }
        if (productoUpdateRequest.birthdate() != null){
            clientToSave.setBirthdate(productoUpdateRequest.birthdate());
        }
        if (productoUpdateRequest.image() != null){
            clientToSave.setImage(productoUpdateRequest.image());
        }
        if (productoUpdateRequest.balance() != null){
            clientToSave.setBalance(productoUpdateRequest.balance());
        }
        if (productoUpdateRequest.isDeleted() != null){
            clientToSave.setIsDeleted(productoUpdateRequest.isDeleted());
        }

        ClientResponse response = clientMapper.toClientResponse(clientRepository.save(clientToSave));
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
    @Transactional
    public ClientResponse updateImage(Long id, MultipartFile image, Boolean withUrl) {
        ClientResponse currentClient = findById(id);
        String imageUrl = fileSystemStorageService.store(image);

        imageUrl = withUrl ? fileSystemStorageService.getUrl(imageUrl) : imageUrl;

        if (currentClient.image() != null && !currentClient.image().isEmpty() && fileSystemStorageService.getUrl(currentClient.image()) != null){
            fileSystemStorageService.delete(currentClient.image());
        }

        Integer rowsAffected = clientRepository.updateImageById(id, imageUrl);

        if (rowsAffected == 0){
            throw new ClientNotFound(id);
        }

        ClientResponse response = ClientResponse.builder()
                .id(id)
                .name(currentClient.name())
                .username(currentClient.username())
                .email(currentClient.email())
                .address(currentClient.address())
                .phone(currentClient.phone())
                .birthdate(currentClient.birthdate())
                .image(imageUrl)
                .balance(currentClient.balance())
                .isDeleted(currentClient.isDeleted())
                .build();

        return response;
    }
}
