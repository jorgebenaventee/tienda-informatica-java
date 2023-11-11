package dev.clownsinformatics.tiendajava.rest.clients.repositories;

import dev.clownsinformatics.tiendajava.rest.clients.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {



    Client findByName(String name);


}
