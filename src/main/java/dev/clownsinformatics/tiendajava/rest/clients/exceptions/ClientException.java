package dev.clownsinformatics.tiendajava.rest.clients.exceptions;

public abstract class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
}
