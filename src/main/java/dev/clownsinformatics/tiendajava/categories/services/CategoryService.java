package dev.clownsinformatics.tiendajava.categories.services;

import dev.clownsinformatics.tiendajava.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.categories.models.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll(String name);

    Category findById(UUID id);

    Category save(CategoryResponseDto category);

    Category update(CategoryResponseDto category, UUID id);

    void delete(UUID id);
}
