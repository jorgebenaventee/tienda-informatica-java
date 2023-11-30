package dev.clownsinformatics.tiendajava.rest.categories.services;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Page<Category> findAll(Optional<String> name, Optional<Boolean> isDeleted, Pageable pageable);

    Category findById(UUID id);

    @Cacheable
    Category findByName(String name);

    Category save(CategoryResponseDto category);

    Category update(CategoryResponseDto category, UUID id);

    void delete(UUID id);
}
