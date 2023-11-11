package dev.clownsinformatics.tiendajava.websockets.notifications.mapper;

import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.websockets.notifications.dto.ProveedoresNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class ProveedoresNotificationMapper {
    public ProveedoresNotificationDto toProveedoresNotificationDto(Proveedor proveedor) {
        return new ProveedoresNotificationDto(
                proveedor.getIdProveedor(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getAddress(),
                proveedor.getDateOfHire().toString(),
                proveedor.getCategory().toString()
        );
    }
}
