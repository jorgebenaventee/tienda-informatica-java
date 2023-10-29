package dev.clownsinformatics.tiendajava.products.controller;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.models.Product;
import dev.clownsinformatics.tiendajava.products.services.ProductService;
import jakarta.validation.Valid;
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

@Slf4j
@RestController
@RequestMapping("/api/productos")
public class ProductosRestController {
    private final ProductService productService;

    @Autowired
    public ProductosRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String name
    ) {
        log.info("Buscando todos los productos con categoria: {} y marca: {}", weight, name);
        return ResponseEntity.ok(productService.findAll(weight, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        log.info("Buscando producto con id: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> postProduct(@Valid @RequestBody ProductCreateDto product) {
        log.info("Creando producto: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Actualizando producto con id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable String id, @Valid @RequestBody ProductUpdateDto product) {
        log.info("Actualizando parcialmente producto con id: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("Eliminando producto con id: {}", id);
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
