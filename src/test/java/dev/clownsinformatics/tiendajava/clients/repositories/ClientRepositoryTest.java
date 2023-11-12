package dev.clownsinformatics.tiendajava.clients.repositories;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import dev.clownsinformatics.tiendajava.rest.clients.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    List<Client> clients = List.of(
            Client.builder()
                    .id(1L)
                    .name("Client 1")
                    .address("Address 1")
                    .email("juancarlos@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image("antigua")
                    .isDeleted(false)
                    .balance(0.0)
                    .username("juancarlos")
                    .build(),
            Client.builder()
                    .id(2L)
                    .name("Client 2")
                    .address("Address 2")
                    .email("ana@gmail.com")
                    .phone("123456789")
                    .birthdate(null)
                    .image(null)
                    .isDeleted(false)
                    .balance(0.0)
                    .username("ana isabel")
                    .build()
    );

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {

        entityManager.clear();

        entityManager.merge(clients.get(0));
        entityManager.merge(clients.get(1));

        entityManager.flush();

    }

    @Test
    void findByIdAndIsDeletedFalseTest() {

        Client client = clientRepository.findByIdAndIsDeletedFalse(1L).orElse(null);

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals(1L, client.getId()),
                () -> assertEquals("Client 1", client.getName()),
                () -> assertEquals("Address 1", client.getAddress()),
                () -> assertEquals("juancarlos@gmail.com", client.getEmail())
                );

    }


    @Test
    void findByUsernameAndIsDeletedFalseTest() {

        Client client = clientRepository.findByUsernameAndIsDeletedFalse("ana isabel");

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals(2L, client.getId()),
                () -> assertEquals("Client 2", client.getName()),
                () -> assertEquals("Address 2", client.getAddress())
        );
    }

    @Test
    void deleteByIdTest() {

        clientRepository.logicalDeleteById(1L);

        Client client = clientRepository.findById(1L).orElse(null);

        assertAll(
                () -> assertNotNull(client),
                () -> assertTrue(client.getIsDeleted())
        );

    }

    @Test
    void updateImageByIdTest(){

        Integer rowsAffected = clientRepository.updateImageById(1L, "nueva");

        Client client = clientRepository.findById(1L).orElse(null);

        assertAll(
                () -> assertEquals(1, rowsAffected),
                () -> assertNotNull(client),
                () -> assertEquals("nueva", client.getImage())
        );

    }

}
