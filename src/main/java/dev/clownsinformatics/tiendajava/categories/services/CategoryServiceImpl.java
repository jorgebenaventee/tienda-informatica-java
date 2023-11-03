package dev.clownsinformatics.tiendajava.categories.services;

import dev.clownsinformatics.tiendajava.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.categories.exceptions.CategoryConflict;
import dev.clownsinformatics.tiendajava.categories.exceptions.CategoryNotFound;
import dev.clownsinformatics.tiendajava.categories.mappers.CategoryMapper;
import dev.clownsinformatics.tiendajava.categories.models.Category;
import dev.clownsinformatics.tiendajava.categories.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private static final String CATEGORY_NOT_FOUND = "Category not found";

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll(String name) {
        if (name == null || name.isEmpty()) {
            log.info("Getting all categories");
            return categoryRepository.findAll();
        } else {
            log.info("Getting all categories with name");
            return categoryRepository.findAllByNameContainingIgnoreCase(name).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
        }
    }

    @Override
    @Cacheable
    public Category findById(UUID id) {
        log.info("Getting category with id: {}", id);
        return categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
    }

    @Override
    @Cacheable
    public Category save(CategoryResponseDto category) {
        log.info("Saving category");
        categoryRepository.findByName(category.name()).ifPresent(c -> {
            throw new CategoryConflict("Category already exists");
        });
        return categoryRepository.save(categoryMapper.toCategory(category));

    }

    @Override
    @Cacheable
    public Category update(CategoryResponseDto category, UUID id) {
        log.info("Updating category with id: {}", id);
        Category categoryToUpdate = categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));

        categoryRepository.findByName(category.name()).ifPresent(c -> {
            if (!c.getUuid().equals(id)) {
                throw new CategoryConflict("Category already exists");
            }
        });
        return categoryRepository.save(categoryMapper.toCategory(category, categoryToUpdate));
    }

    @Override
    @CacheEvict
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting category with id: {}", id);
        Category categoryToUpdate = categoryRepository.findByUuid(id).orElseThrow(() -> new CategoryNotFound(CATEGORY_NOT_FOUND));
        if (categoryRepository.existsProductById(id)) {
            log.warn("Not deleting category with id: {} because it has products", id);
            throw new CategoryConflict("Category has products");
        } else {
            categoryRepository.delete(categoryToUpdate);
        }
    }
}
