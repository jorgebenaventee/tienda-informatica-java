package dev.clownsinformatics.tiendajava.rest.storage.services;

import dev.clownsinformatics.tiendajava.rest.storage.controllers.StorageController;
import dev.clownsinformatics.tiendajava.rest.storage.exceptions.StorageBadRequest;
import dev.clownsinformatics.tiendajava.rest.storage.exceptions.StorageInternal;
import dev.clownsinformatics.tiendajava.rest.storage.exceptions.StorageNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    @Override
    public void init() {
        log.info("Initializing storage service");
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new StorageInternal("Could not initialize storage service" + e.getMessage());
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        String justFilename = filename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension;

        try {
            if (file.isEmpty()) {
                throw new StorageBadRequest("Empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageBadRequest(
                        "Cannot store file with relative path outside current directory"
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                log.info("Saving file " + filename + " as " + storedFilename);
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                        StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }

        } catch (IOException e) {
            throw new StorageInternal("Failed to save file" + filename + " " + e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        log.info("Loading all files");
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageInternal("Fallo al leer ficheros almacenados " + e);
        }
    }

    @Override
    public Path load(String filename) {
        log.info("Loading file " + filename);
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        log.info("Loading file " + filename + " as resource");
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFound("Could not read file " + filename);
            }
        } catch (IOException e) {
            throw new StorageNotFound("Could not read file " + filename + " " + e);
        }
    }

    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try {
            log.info("Deleting file " + filename);
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageInternal("Could not delete file " + filename + " " + e);
        }
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all files");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String getUrl(String filename) {
        log.info("Getting url for file " + filename);
        return MvcUriComponentsBuilder.fromMethodName(StorageController.class, "serveFile", filename,
                null).build().toUriString();
    }
}
