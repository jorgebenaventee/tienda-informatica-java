package dev.clownsinformatics.tiendajava.rest.clients.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion que se lanza cuando no se encuentra un cliente
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFound extends ClientException {
    public ClientNotFound(Long id) {
        super("Client with " + id + " not found.");
    }

}
