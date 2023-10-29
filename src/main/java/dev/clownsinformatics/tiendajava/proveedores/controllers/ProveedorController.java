package dev.clownsinformatics.tiendajava.proveedores.controllers;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.proveedores.services.ProveedorService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@RequestMapping("/api/proveedor")
@RestController
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final Logger logger = Logger.getLogger(ProveedorController.class.getName());

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping()
    public ResponseEntity<List<Proveedor>> getAll(@RequestParam(required = false) String nombre, @RequestParam(required = false) String direccion) {
        log.info("Buscando proveedores...");
        return ResponseEntity.ok(proveedorService.findAll(nombre, direccion));
    }

    @GetMapping
    public Proveedor getProveedorById(@PathVariable Long idProveedor) {
        log.info("Buscando proveedor con ID: " + idProveedor);
        return proveedorService.findById(idProveedor);
    }

    @NonNull
    @PostMapping()
    public ResponseEntity<Proveedor> createProveedor(@Valid @RequestBody ProveedorCreateDto proveedorCreateDto) {
        log.info("Creando proveedor...");
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.save(proveedorCreateDto));
    }

    @NonNull
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable Long idProveedor, @Valid @RequestBody ProveedorUpdateDto proveedorUpdateDto) {
        log.info("Actualizando proveedor con ID: " + idProveedor);
        return ResponseEntity.ok(proveedorService.update(proveedorUpdateDto, idProveedor));
    }

    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedorPatch(@PathVariable Long idProveedor, @Valid @RequestBody ProveedorUpdateDto proveedorUpdateDto) {
        log.info("Actualizando proveedor con ID: " + idProveedor);
        return ResponseEntity.ok(proveedorService.update(proveedorUpdateDto, idProveedor));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long idProveedor) {
        proveedorService.deleteById(idProveedor);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

