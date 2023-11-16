package dev.clownsinformatics.tiendajava.rest.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageNotFound extends StorageException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageNotFound(String id) {
        super("No se encontró el almacenamiento con id: " + id);
    }
}
