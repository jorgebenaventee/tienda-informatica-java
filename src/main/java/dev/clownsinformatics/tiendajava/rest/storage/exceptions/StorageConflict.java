package dev.clownsinformatics.tiendajava.rest.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Excepción de conflicto de almacenamiento.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class StorageConflict extends StorageException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageConflict(String message) {
        super(message);
    }
}
