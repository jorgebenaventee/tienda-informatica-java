package dev.clownsinformatics.tiendajava.rest.storage.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Clase que maneja las excepciones de los archivos
 */
@ControllerAdvice
public class FileExceptionsAdvice {

    /**
     * Recibe una excepción de tamaño de archivo y devuelve una respuesta de error
     * @param exc excepción
     * @return Bad Request
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File too large!");
    }
}