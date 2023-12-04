package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.SuppliersNotificationDto;
import org.springframework.stereotype.Component;

/**
 * Clase que proporciona la funcionalidad de mapeo entre la entidad {@code Supplier} y
 * el objeto de transferencia de datos {@code SuppliersNotificationDto}.
 * Esta clase utiliza el patrón de diseño Mapper para convertir instancias de la entidad
 * {@code Supplier} en instancias de {@code SuppliersNotificationDto}.
 */
@Component
public class SuppliersNotificationMapper {
    /**
     * Convierte una instancia de la entidad {@code Supplier} en un objeto
     * {@code SuppliersNotificationDto}.
     *
     * @param supplier La instancia de la entidad {@code Supplier} a ser convertida.
     * @return Un objeto {@code SuppliersNotificationDto} con la información mapeada.
     */
    public SuppliersNotificationDto toSupplierNotificationDto(Supplier supplier) {
        return new SuppliersNotificationDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getContact(),
                supplier.getAddress(),
                supplier.getDateOfHire().toString(),
                supplier.getCategory().toString()
        );
    }
}
