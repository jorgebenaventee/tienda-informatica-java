package dev.clownsinformatics.tiendajava.rest.auth.exceptions;

/**
 * Excepción que se lanza cuando ocurre un error de autenticación.
 */
public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
