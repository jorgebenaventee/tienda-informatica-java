package dev.clownsinformatics.tiendajava.rest.categories.services;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryConflict;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryNotFound;
import dev.clownsinformatics.tiendajava.rest.categories.mappers.CategoryMapper;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Category not found";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findAll(Optional<String> name, Pageable pageable) {
        log.info("Getting all categories with name: {}", name);
        Specification<Category> specName = (root, criteriaQuery, criteriaBuilder) ->
                name.map(value -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Category> spec = Specification.where(specName);
        return categoryRepository.findAll(spec, pageable);
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
        boolean exists = categoryRepository.existsProductById(id);
        if (exists) {
            log.warn("Not deleting category with id: {} because it has products", id);
            throw new CategoryConflict("Category has products");
        } else {
            categoryRepository.delete(categoryToUpdate);
        }
    }
}
