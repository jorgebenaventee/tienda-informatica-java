package dev.clownsinformatics.tiendajava.rest.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternal extends StorageException{

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageInternal(String message) {
        super(message);
    }
}
