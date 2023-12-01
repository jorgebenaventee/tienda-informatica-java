package dev.clownsinformatics.tiendajava.rest.storage.services;

import dev.clownsinformatics.tiendajava.rest.storage.controllers.StorageController;
import dev.clownsinformatics.tiendajava.rest.storage.exceptions.StorageBadRequest;
import dev.clownsinformatics.tiendajava.rest.storage.exceptions.StorageConflict;
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

/**
 * Servicio de almacenamiento de archivos en el sistema de archivos del servidor.
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    /**
     * Constructor del servicio de almacenamiento de archivos en el sistema de archivos del servidor.
     * @param path Ruta del directorio raíz de almacenamiento de archivos.
     */
    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    /**
     * Inicializa el servicio de almacenamiento de archivos en el sistema de archivos del servidor.
     */
    @Override
    public void init() {
        log.info("Initializing storage service");
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new StorageConflict("Could not initialize storage service" + e.getMessage());
        }
    }

    /**
     * Almacena un archivo en el sistema de archivos del servidor.
     * @param file Archivo a almacenar.
     * @return Nombre del archivo almacenado.
     */
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
                throw new StorageBadRequest("Cannot store file with relative path outside current directory" + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                storedFilename = storedFilename.replace(" ", "_");
                log.info("Saving file " + filename + " as " + storedFilename);
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }

        } catch (IOException e) {
            throw new StorageConflict("Failed to save file " + filename + " " + e);
        }
    }

    /**
     * Carga todos los archivos almacenados en el sistema de archivos del servidor.
     * @return Lista de archivos almacenados.
     */
    @Override
    public Stream<Path> loadAll() {
        log.info("Loading all files");
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageConflict("Failed to read saved files " + e);
        }
    }

    /**
     * Carga un archivo almacenado en el sistema de archivos del servidor.
     * @param filename Nombre del archivo a cargar.
     * @return Ruta del archivo cargado.
     */
    @Override
    public Path load(String filename) {
        log.info("Loading file " + filename);
        return rootLocation.resolve(filename);
    }

    /**
     * Carga un archivo almacenado en el sistema de archivos del servidor como recurso.
     * @param filename Nombre del archivo a cargar.
     * @return Recurso del archivo cargado.
     */
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

    /**
     * Elimina un archivo almacenado en el sistema de archivos del servidor.
     * @param filename Nombre del archivo a eliminar.
     */
    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try {
            log.info("Deleting file " + filename);
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageConflict("Could not delete file " + filename + " " + e);
        }
    }

    /**
     * Elimina todos los archivos almacenados en el sistema de archivos del servidor.
     */
    @Override
    public void deleteAll() {
        log.info("Deleting all files");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Obtiene la URL de un archivo almacenado en el sistema de archivos del servidor.
     * @param filename Nombre del archivo.
     * @return URL del archivo.
     */
    @Override
    public String getUrl(String filename) {
        log.info("Getting url for file " + filename);
        return MvcUriComponentsBuilder.fromMethodName(StorageController.class, "serveFile", filename,
                null).build().toUriString();
    }
}
