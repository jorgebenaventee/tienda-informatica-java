package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.SuppliersNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class SuppliersNotificationMapper {
    public SuppliersNotificationDto toProveedoresNotificationDto(Supplier proveedor) {
        return new SuppliersNotificationDto(
                proveedor.getId(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getAddress(),
                proveedor.getDateOfHire().toString(),
                proveedor.getCategory().toString()
        );
    }
}
