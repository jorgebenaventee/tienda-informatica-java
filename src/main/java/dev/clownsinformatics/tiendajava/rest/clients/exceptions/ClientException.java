package dev.clownsinformatics.tiendajava.rest.clients.exceptions;

/**
 * Expcecion generica de clientes
 */
public abstract class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
