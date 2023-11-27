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

@Slf4j
@RequestMapping("/api/suppliers")
@RestController
public class SupplierController {

    private final SupplierService supplierService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public SupplierController(SupplierService supplierService, PaginationLinksUtils paginationLinksUtils) {
        this.supplierService = supplierService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

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

    @Operation(summary = "Create a new supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
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

    @Operation(summary = "Update a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
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

    @Operation(summary = "Update a supplier partially")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
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

    @Operation(summary = "Delete a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "UUID of the supplier", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String id) {
        supplierService.deleteByUUID(id);
        return ResponseEntity.noContent().build();
    }

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

