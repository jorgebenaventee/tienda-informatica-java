package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.CategoryNotificationDto;
import org.springframework.stereotype.Component;

/**
 * Clase que proporciona la funcionalidad de mapeo entre la entidad {@code Category} y
 * el objeto de transferencia de datos {@code CategoryNotificationDto}.
 * Esta clase utiliza el patrón de diseño Mapper para convertir instancias de la entidad
 * {@code Category} en instancias de {@code CategoryNotificationDto}.
 */
@Component
public class CategoryNotificationMapper {

    /**
     * Convierte una instancia de la entidad {@code Category} en un objeto
     * {@code CategoryNotificationDto}.
     *
     * @param category La instancia de la entidad {@code Category} a ser convertida.
     * @return Un objeto {@code CategoryNotificationDto} con la información mapeada.
     */
    public CategoryNotificationDto toCategoryNotificationDto(Category category) {
        return new CategoryNotificationDto(
                category.getUuid(),
                category.getName()
        );
    }
}
