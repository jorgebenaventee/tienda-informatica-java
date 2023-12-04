package dev.clownsinformatics.tiendajava.rest.suppliers.mapper;

import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de mapear entre objetos DTO y entidades Supplier.
 * Se utiliza para convertir entre objetos de datos de proveedores (DTO) y la entidad Supplier.
 *
 * @Component Indica que esta clase es un componente de Spring y puede ser gestionada por el contenedor de Spring.
 */
@Component
public class SupplierMapper {

    /**
     * Convierte un objeto SupplierCreateDto a una entidad Supplier.
     *
     * @param supplierCreateDto DTO con la información para crear un proveedor.
     * @return Entidad Supplier creada a partir del DTO.
     */
    public Supplier toSupplier(SupplierCreateDto supplierCreateDto) {
        return Supplier.builder()
                .name(supplierCreateDto.name())
                .contact(supplierCreateDto.contact())
                .address(supplierCreateDto.address())
                .category(supplierCreateDto.category())
                .isDeleted(supplierCreateDto.isDeleted() != null ? supplierCreateDto.isDeleted() : Boolean.FALSE)
                .build();
    }

    /**
     * Convierte un objeto SupplierUpdateDto y una entidad Supplier a una nueva entidad Supplier actualizada.
     *
     * @param supplierUpdateDto DTO con la información para actualizar un proveedor.
     * @param supplier          Entidad Supplier existente.
     * @return Entidad Supplier actualizada a partir del DTO y la entidad existente.
     */
    public Supplier toSupplier(SupplierUpdateDto supplierUpdateDto, Supplier supplier) {
        return Supplier.builder()
                .id(supplier.getId())
                .name(supplierUpdateDto.name() != null ? supplierUpdateDto.name() : supplier.getName())
                .contact(supplierUpdateDto.contact() != null ? supplierUpdateDto.contact() : supplier.getContact())
                .address(supplierUpdateDto.address() != null ? supplierUpdateDto.address() : supplier.getAddress())
                .category(supplierUpdateDto.category() != null ? supplierUpdateDto.category() : supplier.getCategory())
                .isDeleted(supplierUpdateDto.isDeleted() != null ? supplierUpdateDto.isDeleted() : supplier.getIsDeleted())
                .build();
    }

    /**
     * Convierte un objeto SupplierResponseDto a una entidad Supplier.
     *
     * @param supplierResponseDto DTO con la información de respuesta de un proveedor.
     * @return Entidad Supplier creada a partir del DTO de respuesta.
     */
    public Supplier toSupplier(SupplierResponseDto supplierResponseDto) {
        return Supplier.builder()
                .id(supplierResponseDto.id())
                .name(supplierResponseDto.name())
                .contact(supplierResponseDto.contact())
                .address(supplierResponseDto.address())
                .category(supplierResponseDto.category())
                .build();
    }

    /**
     * Convierte una entidad Supplier a un objeto SupplierResponseDto.
     *
     * @param supplier Entidad Supplier.
     * @return DTO de respuesta con la información del proveedor.
     */
    public SupplierResponseDto toSupplierDto(Supplier supplier) {
        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getContact(),
                supplier.getAddress(),
                supplier.getDateOfHire(),
                supplier.getCategory(),
                supplier.getIsDeleted()
        );
    }

}