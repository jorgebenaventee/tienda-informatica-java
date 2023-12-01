package dev.clownsinformatics.tiendajava.rest.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Excepción de solicitud incorrecta de almacenamiento.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends StorageException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageBadRequest(String message) {
        super(message);
    }
}
