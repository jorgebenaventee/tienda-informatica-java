package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProductsNotificationDto;
import org.springframework.stereotype.Component;

/**
 * Clase que proporciona la funcionalidad de mapeo entre la entidad {@code Product} y
 * el objeto de transferencia de datos {@code ProductsNotificationDto}.
 * Esta clase utiliza el patrón de diseño Mapper para convertir instancias de la entidad
 * {@code Product} en instancias de {@code ProductsNotificationDto}.
 */
@Component
public class ProductNotificationMapper {

    /**
     * Convierte una instancia de la entidad {@code Product} en un objeto
     * {@code ProductsNotificationDto}.
     *
     * @param product La instancia de la entidad {@code Product} a ser convertida.
     * @return Un objeto {@code ProductsNotificationDto} con la información mapeada.
     */
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
