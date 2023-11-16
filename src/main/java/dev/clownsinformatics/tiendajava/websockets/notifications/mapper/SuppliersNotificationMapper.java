package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.SuppliersNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class SuppliersNotificationMapper {
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
