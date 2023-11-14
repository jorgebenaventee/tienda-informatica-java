package dev.clownsinformatics.tiendajava.rest.categories.mappers;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void toCategory() {
        CategoryResponseDto categoryCreateDto = new CategoryResponseDto("Disney");

        var res = categoryMapper.toCategory(categoryCreateDto);

        assertAll(
                () -> assertEquals(categoryCreateDto.name(), res.getName())
        );
    }

    @Test
    void testToCategory() {
        CategoryResponseDto categoryCreateDto = new CategoryResponseDto("Disney");

        Category category = Category.builder()
                .uuid(UUID.randomUUID())
                .name("Disney")
                .build();

        var res = categoryMapper.toCategory(categoryCreateDto, category);

        assertAll(
                () -> assertEquals(categoryCreateDto.name(), res.getName())
        );
    }
}