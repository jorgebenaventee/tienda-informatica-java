package dev.clownsinformatics.tiendajava.categories.controllers;

import dev.clownsinformatics.tiendajava.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.categories.models.Category;
import dev.clownsinformatics.tiendajava.categories.services.CategoryService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Slf4j
public class CategoryRestController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(categoryService.findAll(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Category> save(@Valid @RequestBody CategoryResponseDto category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@Valid @RequestBody CategoryResponseDto category, @PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.update(category, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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