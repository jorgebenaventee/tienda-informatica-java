package dev.clownsinformatics.tiendajava.rest.clients.repositories;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {


    Optional<Client> findByIdAndIsDeletedFalse(Long id);

    Client findAllByIsDeletedFalse();

    Client findByNameAndIsDeletedFalse(String name);

    @Modifying
    @Query("UPDATE Client c SET c.image = ?2 WHERE c.id = ?1")
    Client updateImageById(Long id, String image);


}
