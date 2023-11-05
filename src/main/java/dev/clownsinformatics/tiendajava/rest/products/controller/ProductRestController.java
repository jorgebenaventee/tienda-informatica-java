package dev.clownsinformatics.tiendajava.rest.products.controller;

import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.services.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/productos")
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String name
    ) {
        log.info("Searching all products with category: {} and brand: {}", weight, name);
        return ResponseEntity.ok(productService.findAll(weight, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        log.info("Searching product with id: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> postProduct(@Valid @RequestBody ProductCreateDto product) {
        log.info("Creating product: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> putProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Updating product with id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> patchProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Updating partial product with id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> patchProductImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        log.info("Updating product image with id: {}", id);
        return ResponseEntity.ok(productService.updateImage(id, file));
    }

    @DeleteMapping("/{id}")
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
