package dev.clownsinformatics.tiendajava.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando las contraseñas no coinciden.
 * Status 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotMatch extends RuntimeException {
    public PasswordNotMatch(String message) {
        super(message);
    }
}
