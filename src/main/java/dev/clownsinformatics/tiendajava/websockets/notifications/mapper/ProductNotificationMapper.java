package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProductsNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class ProductNotificationMapper {
    public ProductsNotificationDto toProductNotificationDto(Product product) {
        return new ProductsNotificationDto(
                product.getId(),
                product.getName(),
                product.getWeight(),
                product.getPrice(),
                product.getImg(),
                product.getStock(),
                product.getDescription(),
                product.getCategory()
        );
    }
}
