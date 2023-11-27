package dev.clownsinformatics.tiendajava.rest.products.controller;

import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.services.ProductService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/products")
@PreAuthorize("hasRole('USER')")
public class ProductRestController {
    private final ProductService productService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public ProductRestController(ProductService productService, PaginationLinksUtils paginationLinksUtils) {
        this.productService = productService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the products"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Products not found")
    })
    @Parameters({
            @Parameter(name = "name", description = "Product name"),
            @Parameter(name = "maxWeight", description = "Maximum product weight"),
            @Parameter(name = "maxPrice", description = "Maximum product price"),
            @Parameter(name = "minStock", description = "Minimum product stock"),
            @Parameter(name = "category", description = "Product category"),
            @Parameter(name = "page", description = "Page number"),
            @Parameter(name = "size", description = "Page size"),
            @Parameter(name = "sortBy", description = "Sort by"),
            @Parameter(name = "direction", description = "Sort direction")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDto>> getAllProducts(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Double> maxWeight,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(required = false) Optional<Double> minStock,
            @RequestParam(required = false) Optional<String> category,
            @RequestParam(defaultValue = "false", required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Searching all products with name: {}, maxWeight: {}, maxPrice: {}, minStock: {}, category: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                name, maxWeight, maxPrice, minStock, category, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ProductResponseDto> pageResult = productService.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @Operation(summary = "Get a product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "Product id", required = true)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String id) {
        log.info("Searching product with id: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "Get a product by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "product", description = "Product body", required = true)
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> postProduct(@Valid @RequestBody ProductCreateDto product) {
        log.info("Creating product: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @Operation(summary = "Get a product by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "productUpdate", description = "Product update body", required = true)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> putProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Updating product with id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @Operation(summary = "Get a product by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "productUpdate", description = "Product update body", required = true)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> patchProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Updating partial product with id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @Operation(summary = "Get a product by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "file", description = "Product image", required = true)
    })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> patchProductImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        log.info("Updating product image with id: {}", id);
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.updateImage(id, file));
    }

    @Operation(summary = "Get a product by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "Product id", required = true)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteById(id);
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
