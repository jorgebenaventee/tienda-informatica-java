package dev.clownsinformatics.tiendajava.rest.suppliers.controllers;

import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.services.SupplierService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para gestionar operaciones relacionadas con proveedores.
 *
 * @Slf4j Habilita el registro de eventos utilizando la anotación SLF4J.
 * @RequestMapping("/api/suppliers") Mapea la ruta base para todas las operaciones del controlador.
 * @RestController Indica que esta clase es un controlador REST.
 */
@Slf4j
@RequestMapping("/api/suppliers")
@RestController
public class SupplierController {

    /**
     * Servicio de proveedores utilizado para realizar operaciones relacionadas con proveedores.
     */
    private final SupplierService supplierService;
    /**
     * Utilidad para la creación de enlaces de paginación.
     */
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor que inyecta las dependencias necesarias para el controlador.
     *
     * @param supplierService       Servicio de proveedores.
     * @param paginationLinksUtils Utilidad para enlaces de paginación.
     */
    @Autowired
    public SupplierController(SupplierService supplierService, PaginationLinksUtils paginationLinksUtils) {
        this.supplierService = supplierService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Recupera todos los proveedores con opciones de filtrado y paginación.
     *
     * @param category  Categoría del proveedor (opcional).
     * @param name      Nombre del proveedor (opcional).
     * @param contact   Contacto del proveedor (opcional).
     * @param isDeleted Indica si el proveedor está eliminado (opcional).
     * @param page      Número de página.
     * @param size      Número de elementos por página.
     * @param sortBy    Campo por el cual ordenar los resultados.
     * @param direction Dirección de ordenamiento (ascendente o descendente).
     * @param request   Objeto HttpServletRequest para construir enlaces de paginación.
     * @return ResponseEntity con la lista paginada de proveedores y enlaces de paginación.
     */
    @Operation(summary = "Get all suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the suppliers"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Suppliers not found")
    })
    @Parameters({
            @Parameter(name = "category", description = "Category of the supplier", example = "PORTATILES"),
            @Parameter(name = "name", description = "Name of the supplier", example = "HP"),
            @Parameter(name = "contact", description = "Contact of the supplier", example = "123456789"),
            @Parameter(name = "isDeleted", description = "Supplier deleted", example = "false"),
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Number of elements per page", example = "10"),
            @Parameter(name = "sortBy", description = "Sort by field", example = "id"),
            @Parameter(name = "direction", description = "Sort direction", example = "asc")
    })
    @GetMapping()
    public ResponseEntity<PageResponse<SupplierResponseDto>> getAll(@RequestParam(required = false) Optional<String> category,
                                                                    @RequestParam(required = false) Optional<String> name,
                                                                    @RequestParam(required = false) Optional<Integer> contact,
                                                                    @RequestParam(defaultValue = "false") String isDeleted,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction,
                                                                    HttpServletRequest request) {
        log.info("Searching suppliers...");
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<SupplierResponseDto> result = supplierService.findAll(category, name, contact, Optional.of(Boolean.valueOf(isDeleted)), PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(result, uriBuilder))
                .body(PageResponse.of(result, sortBy, direction));
    }

    /**
     * Obtiene un proveedor por su UUID.
     *
     * @param id UUID del proveedor.
     * @return Proveedor encontrado.
     */
    @Operation(summary = "Get supplier by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the supplier"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "UUID of the supplier", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    })
    @GetMapping("/{id}")
    public SupplierResponseDto getSupplierByUUID(@PathVariable String id) {
        log.info("Searching suppliers with ID: " + id);
        return supplierService.findByUUID(id);
    }

    /**
     * Crea un nuevo proveedor.
     *
     * @param supplierCreateDto DTO con la información del proveedor a crear.
     * @return ResponseEntity con el proveedor creado.
     */
    @Operation(summary = "Create a new supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "supplierCreateDto", description = "Supplier to create", required = true)
    })
    @NonNull
    @PostMapping()
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierCreateDto supplierCreateDto) {
        log.info("Creating suppliers...");
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.save(supplierCreateDto));
    }

    /**
     * Actualiza un proveedor por su UUID.
     *
     * @param id                UUID del proveedor a actualizar.
     * @param supplierUpdateDto DTO con la información actualizada del proveedor.
     * @return ResponseEntity con el proveedor actualizado.
     */
    @Operation(summary = "Update a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "UUID of the supplier", example = "123e4567-e89b-12d3-a456-426614174000", required = true),
            @Parameter(name = "supplierUpdateDto", description = "Supplier to update", required = true)
    })
    @NonNull
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(@PathVariable String id, @Valid @RequestBody SupplierUpdateDto supplierUpdateDto) {
        log.info("Updating suppliers with ID: " + id);
        return ResponseEntity.ok(supplierService.update(supplierUpdateDto, id));
    }

    /**
     * Actualiza parcialmente un proveedor por su UUID.
     *
     * @param id                UUID del proveedor a actualizar parcialmente.
     * @param supplierUpdateDto DTO con la información parcialmente actualizada del proveedor.
     * @return ResponseEntity con el proveedor parcialmente actualizado.
     */
    @Operation(summary = "Update a supplier partially")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "UUID of the supplier", example = "123e4567-e89b-12d3-a456-426614174000", required = true),
            @Parameter(name = "supplierUpdateDto", description = "Supplier to update", required = true)
    })
    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplierPatch(@PathVariable String id, @Valid @RequestBody SupplierUpdateDto supplierUpdateDto) {
        log.info("Updating suppliers partially with ID: " + id);
        return ResponseEntity.ok(supplierService.update(supplierUpdateDto, id));
    }

    /**
     * Elimina un proveedor por su UUID.
     *
     * @param id UUID del proveedor a eliminar.
     * @return ResponseEntity indicando el éxito de la operación.
     */
    @Operation(summary = "Delete a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "UUID of the supplier", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String id) {
        supplierService.deleteByUUID(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja excepciones de validación y devuelve un mapa de errores.
     *
     * @param ex Excepción de validación de argumentos del método.
     * @return Mapa de errores detallando los problemas de validación.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

