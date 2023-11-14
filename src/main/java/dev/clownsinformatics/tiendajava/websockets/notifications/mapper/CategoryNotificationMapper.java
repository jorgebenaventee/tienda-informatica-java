package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.CategoryNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryNotificationMapper {
    public CategoryNotificationDto toCategoryNotificationDto(Category category) {
        return new CategoryNotificationDto(
                category.getUuid(),
                category.getName()
        );
    }
}
