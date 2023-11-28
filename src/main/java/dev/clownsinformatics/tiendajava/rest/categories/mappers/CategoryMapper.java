package dev.clownsinformatics.tiendajava.rest.categories.mappers;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Componente encargado de mapear entre objetos DTO (Data Transfer Object) y entidades de categoría.
 * Realiza la conversión entre {@link CategoryResponseDto} y {@link Category}.
 *
 * @version 1.0
 * @since 2023-11-28
 */
@Component
public class CategoryMapper {
    /**
     * Convierte un DTO de categoría ({@link CategoryResponseDto}) en una entidad de categoría ({@link Category}).
     *
     * @param categoryDto DTO de categoría que contiene la información a mapear.
     * @return Entidad de categoría creada a partir del DTO.
     */
    public Category toCategory(CategoryResponseDto categoryDto) {
        return new Category(null, categoryDto.name(), LocalDateTime.now(), LocalDateTime.now(), false);
    }

    /**
     * Actualiza una entidad de categoría existente ({@link Category}) con la información del DTO de categoría proporcionado.
     *
     * @param categoryDto DTO de categoría que contiene la información actualizada.
     * @param category    Entidad de categoría existente que se actualizará.
     * @return Entidad de categoría actualizada.
     */
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