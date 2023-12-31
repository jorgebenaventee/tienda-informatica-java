package dev.clownsinformatics.tiendajava.rest.storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Clase que maneja las excepciones de tamaño de archivo
 * @version 1.0.0
 * @since 1.0.0
 */
@ControllerAdvice
public class FileExceptionsAdvice {

    /**
     * Maneja la excepción de tamaño de archivo
     * @param exc excepción de tamaño de archivo
     * @return respuesta de error
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File too large!");
    }
}