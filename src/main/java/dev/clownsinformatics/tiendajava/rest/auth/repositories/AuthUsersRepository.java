package dev.clownsinformatics.tiendajava.rest.auth.repositories;

import dev.clownsinformatics.tiendajava.rest.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//extends JpaRepository<User, Long> para
@Repository
public interface AuthUsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}