package dev.clownsinformatics.tiendajava.rest.proveedores.controllers;

import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.SupplierService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
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

    @GetMapping()
    public ResponseEntity<PageResponse<SupplierResponseDto>> getAll(@RequestParam(required = false) Optional<String> category,
                                                                    @RequestParam(required = false) Optional<String> name,
                                                                    @RequestParam(required = false) Optional<Integer> contact,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction,
                                                                    HttpServletRequest request) {
        log.info("Searching suppliers...");
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<SupplierResponseDto> result = supplierService.findAll(category, name, contact, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(result, uriBuilder))
                .body(PageResponse.of(result, sortBy, direction));
    }

    @GetMapping("/{id}")
    public SupplierResponseDto getSupplierByUUID(@PathVariable String id) {
        log.info("Searching suppliers with ID: " + id);
        return supplierService.findByUUID(id);
    }

    @NonNull
    @PostMapping()
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierCreateDto proveedorCreateDto) {
        log.info("Creating suppliers...");
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.save(proveedorCreateDto));
    }

    @NonNull
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(@PathVariable String id, @Valid @RequestBody SupplierUpdateDto proveedorUpdateDto) {
        log.info("Updating suppliers with ID: " + id);
        return ResponseEntity.ok(supplierService.update(proveedorUpdateDto, id));
    }

    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplierPatch(@PathVariable String id, @Valid @RequestBody SupplierUpdateDto proveedorUpdateDto) {
        log.info("Updating suppliers with ID: " + id);
        return ResponseEntity.ok(supplierService.update(proveedorUpdateDto, id));
    }


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
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

