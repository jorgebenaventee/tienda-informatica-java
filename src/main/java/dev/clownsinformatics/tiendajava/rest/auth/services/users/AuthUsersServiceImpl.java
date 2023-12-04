package dev.clownsinformatics.tiendajava.rest.auth.services.users;

import dev.clownsinformatics.tiendajava.rest.auth.repositories.AuthUsersRepository;
import dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación de la interfaz {@link AuthUsersService}. Es el servicio que utiliza Spring Security para obtener los detalles de un usuario
 */
@Service("userDetailsService")
public class AuthUsersServiceImpl implements AuthUsersService {

    private final AuthUsersRepository authUsersRepository;

    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFound {
        return authUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User with username " + username + " not found"));
    }
}