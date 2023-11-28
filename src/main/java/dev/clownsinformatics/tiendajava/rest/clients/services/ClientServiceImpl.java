package dev.clownsinformatics.tiendajava.rest.clients.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientCreateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientUpdateRequest;
import dev.clownsinformatics.tiendajava.rest.clients.exceptions.ClientNotFound;
import dev.clownsinformatics.tiendajava.rest.clients.mappers.ClientMapper;
import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.FileSystemStorageService;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ClientNotificationDto;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ClientNotificationMapper;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "clients")
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final FileSystemStorageService fileSystemStorageService;
    private final ClientNotificationMapper clientNotificationMapper;
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper = new ObjectMapper();
    private WebSocketHandler webSocketService;


    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, FileSystemStorageService fileSystemStorageService, ClientNotificationMapper clientNotificationMapper, WebSocketConfig webSocketConfig) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.fileSystemStorageService = fileSystemStorageService;
        this.clientNotificationMapper = clientNotificationMapper;
        this.webSocketConfig = webSocketConfig;
        this.webSocketService = webSocketConfig.webSocketClientHandler();
    }


    @Override
    public Page<ClientResponse> findAll(Optional<String> username, Optional<Boolean> isDeleted, Pageable pageable) {

        Specification<Client> specUsername = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Client> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

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
    @CachePut(key = "#result.id")
    public ClientResponse save(ClientCreateRequest productoCreateRequest) {
        ClientResponse response = clientMapper.toClientResponse(clientRepository.save(clientMapper.toClient(productoCreateRequest)));
        sendNotification(Notification.Tipo.CREATE, clientMapper.toClient(response));
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

        if (productoUpdateRequest.name() != null) {
            clientToSave.setName(productoUpdateRequest.name());
        }
        if (productoUpdateRequest.username() != null) {
            clientToSave.setUsername(productoUpdateRequest.username());
        }
        if (productoUpdateRequest.email() != null) {
            clientToSave.setEmail(productoUpdateRequest.email());
        }
        if (productoUpdateRequest.address() != null) {
            clientToSave.setAddress(productoUpdateRequest.address());
        }
        if (productoUpdateRequest.phone() != null) {
            clientToSave.setPhone(productoUpdateRequest.phone());
        }
        if (productoUpdateRequest.birthdate() != null) {
            clientToSave.setBirthdate(LocalDate.parse(productoUpdateRequest.birthdate()));
        }
        if (productoUpdateRequest.image() != null) {
            clientToSave.setImage(productoUpdateRequest.image());
        }
        if (productoUpdateRequest.balance() != null) {
            clientToSave.setBalance(productoUpdateRequest.balance());
        }
        if (productoUpdateRequest.isDeleted() != null) {
            clientToSave.setIsDeleted(productoUpdateRequest.isDeleted());
        }

        ClientResponse response = clientMapper.toClientResponse(clientRepository.save(clientToSave));

        sendNotification(Notification.Tipo.UPDATE, clientMapper.toClient(response));

        return response;
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        var client = clientRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ClientNotFound(id));
        clientRepository.deleteById(id);
        if (client.getImage() != null && !client.getImage().isEmpty() && fileSystemStorageService.getUrl(client.getImage()) != null) {
            fileSystemStorageService.delete(client.getImage());
        }
        sendNotification(Notification.Tipo.DELETE, client);
    }

    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public ClientResponse updateImage(Long id, MultipartFile image, Boolean withUrl) {
        ClientResponse currentClient = findById(id);
        String imageUrl = fileSystemStorageService.store(image);

        imageUrl = withUrl ? fileSystemStorageService.getUrl(imageUrl) : imageUrl;

        if (currentClient.image() != null && !currentClient.image().isEmpty() && fileSystemStorageService.getUrl(currentClient.image()) != null) {
            fileSystemStorageService.delete(currentClient.image());
        }

        Integer rowsAffected = clientRepository.updateImageById(id, imageUrl);

        if (rowsAffected == 0) {
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

        sendNotification(Notification.Tipo.UPDATE, clientMapper.toClient(response));

        return response;
    }

    void sendNotification(Notification.Tipo tipo, Client data) {
        if (webSocketService == null) {
            log.warn("No se ha configurado el servicio de websockets");
            webSocketService = this.webSocketConfig.webSocketClientHandler();
        }
        try {
            Notification<ClientNotificationDto> notificacion = new Notification<>(
                    "CLIENTS",
                    tipo,
                    clientNotificationMapper.toClientNotificationDto(data),
                    LocalDate.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));
            log.info("Enviando notificación mensaje..");

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }


}
