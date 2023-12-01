package dev.clownsinformatics.tiendajava.config.storage;

import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Clase de configuración para el almacenamiento de archivos
 * en el servidor.
 */
@Configuration
@Slf4j
public class StorageConfig {
    private final StorageService storageService;

    /**
     * Indica si se deben eliminar todos los archivos en el directorio
     * de almacenamiento.
     */
    @Value("${upload.delete}")
    private String deleteAll;

    /**
     * Constructor de la clase.
     * @param storageService Servicio de almacenamiento.
     */
    @Autowired
    public StorageConfig(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Inicializa el directorio de almacenamiento.
     */
    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Deleting all files in upload directory...");
            storageService.deleteAll();
        }

        storageService.init();
    }
}
