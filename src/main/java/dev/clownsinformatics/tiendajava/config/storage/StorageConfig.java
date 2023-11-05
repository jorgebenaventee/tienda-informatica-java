package dev.clownsinformatics.tiendajava.config.storage;

import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StorageConfig {
    private final StorageService storageService;

    @Value("${upload.delete}")
    private String deleteAll;

    @Autowired
    public StorageConfig(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Deleting all files in upload directory...");
            storageService.deleteAll();
        }

        storageService.init();
    }
}
