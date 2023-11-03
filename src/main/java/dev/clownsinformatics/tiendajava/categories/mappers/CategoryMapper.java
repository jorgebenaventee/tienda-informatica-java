package dev.clownsinformatics.tiendajava.categories.mappers;

import dev.clownsinformatics.tiendajava.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.categories.models.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryResponseDto categoryDto) {
        return new Category(null, categoryDto.name(), LocalDateTime.now(), LocalDateTime.now());
    }

    public Category toCategory(CategoryResponseDto categoryDto, Category category) {
        return new Category(
                category.getUuid(),
                categoryDto.name() != null ? categoryDto.name() : category.getName(),
                category.getCreatedAt(),
                LocalDateTime.now()
        );
    }
}