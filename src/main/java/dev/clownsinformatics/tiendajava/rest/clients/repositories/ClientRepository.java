package dev.clownsinformatics.tiendajava.rest.clients.repositories;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repositorio de la entidad Client
 * Se usa para implementar ciertas consultas personalizadas
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {


    Optional<Client> findByIdAndIsDeletedFalse(Long id);

    Client findByUsernameAndIsDeletedFalse(String name);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Client c SET c.image = :image WHERE c.id = :id")
    Integer updateImageById(@Param("id") Long id, @Param("image") String image);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Client c SET c.isDeleted = true WHERE c.id = :id")
    void deleteById(@Param("id") Long id);


}
