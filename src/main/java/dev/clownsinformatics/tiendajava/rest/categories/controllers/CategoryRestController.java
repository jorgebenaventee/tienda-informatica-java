package dev.clownsinformatics.tiendajava.rest.categories.controllers;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador que gestiona las operaciones relacionadas con las categorías en la API REST.
 * Proporciona puntos finales para la consulta, creación, actualización y eliminación de categorías.
 *
 * @version 1.0
 * @since 2023-11-28
 */
@RestController
@RequestMapping("/api/categories")
@Slf4j
@PreAuthorize("hasRole('USER')")
public class CategoryRestController {
    private final CategoryService categoryService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public CategoryRestController(CategoryService categoryService, PaginationLinksUtils paginationLinksUtils) {
        this.categoryService = categoryService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todas las categorías paginadas y opcionalmente filtradas por nombre y estado de eliminación.
     *
     * @param name      Nombre opcional para filtrar las categorías.
     * @param isDeleted Estado de eliminación opcional para filtrar las categorías.
     * @param page      Número de página solicitada (predeterminado: 0).
     * @param size      Tamaño de la página solicitada (predeterminado: 10).
     * @param sortBy    Campo por el cual ordenar las categorías (predeterminado: uuid).
     * @param direction Dirección de ordenamiento (ascendente o descendente, predeterminado: asc).
     * @param request   Objeto HttpServletRequest para construir enlaces de paginación.
     * @return Respuesta que contiene una página de categorías y enlaces de paginación.
     */
    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the categories"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Categories not found")
    })
    @Parameters({
            @Parameter(name = "name", description = "Category name"),
            @Parameter(name = "page", description = "Page number"),
            @Parameter(name = "size", description = "Page size"),
            @Parameter(name = "sortBy", description = "Sort by"),
            @Parameter(name = "direction", description = "Sort direction")
    })
    @GetMapping
    public ResponseEntity<PageResponse<Category>> getAll(@RequestParam(required = false) Optional<String> name,
                                                         @RequestParam(required = false) Optional<Boolean> isDeleted,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "uuid") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String direction,
                                                         HttpServletRequest request
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Category> categoriaPage = categoryService.findAll(name, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(categoriaPage, uriBuilder))
                .body(PageResponse.of(categoriaPage, sortBy, direction));
    }

    /**
     * Obtiene una categoría por su identificador UUID.
     *
     * @param id Identificador UUID de la categoría.
     * @return Respuesta que contiene la categoría encontrada.
     */
    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "Category id", required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    /**
     * Crea una nueva categoría a partir de los datos proporcionados en el DTO de respuesta de categoría.
     *
     * @param category DTO de respuesta de categoría que contiene la información para crear la nueva categoría.
     * @return Respuesta que contiene la categoría creada.
     */
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "409", description = "Category already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "category", description = "Category to create", required = true)
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> save(@Valid @RequestBody CategoryResponseDto category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    /**
     * Actualiza una categoría existente con la información proporcionada en el DTO de respuesta de categoría.
     *
     * @param category DTO de respuesta de categoría que contiene la información actualizada.
     * @param id       Identificador UUID de la categoría a actualizar.
     * @return Respuesta que contiene la categoría actualizada.
     */
    @Operation(summary = "Update a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "category", description = "Category to update", required = true),
            @Parameter(name = "id", description = "Category id", required = true)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(@Valid @RequestBody CategoryResponseDto category, @PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.update(category, id));
    }

    /**
     * Elimina una categoría por su identificador UUID.
     *
     * @param id Identificador UUID de la categoría a eliminar.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Parameters({
            @Parameter(name = "id", description = "Category id", required = true)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Maneja las excepciones de validación y devuelve un mapa de errores.
     *
     * @param ex Excepción de validación.
     * @return Mapa que contiene los errores de validación.
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