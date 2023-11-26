package dev.clownsinformatics.tiendajava.rest.categories.mappers;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryResponseDto categoryDto) {
        return new Category(null, categoryDto.name(), LocalDateTime.now(), LocalDateTime.now(), false);
    }

    public Category toCategory(CategoryResponseDto categoryDto, Category category) {
        return new Category(
                category.getUuid(),
                categoryDto.name() != null ? categoryDto.name() : category.getName(),
                category.getCreatedAt(),
                LocalDateTime.now(),
                category.isDeleted()
        );
    }
}