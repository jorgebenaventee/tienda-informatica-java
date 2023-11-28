package dev.clownsinformatics.tiendajava.rest.suppliers.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//estos atributos son los que se van a recibir en el body de la peticion,tienen validaciones
public record SupplierCreateDto(
        @NotBlank(message = "The name cannot be blank")
        @Schema(description = "The name of the supplier", example = "Eva")
        String name,

        @Min(value = 0, message = "The contact cannot be less than 0")
        @Schema(description = "The contact of the supplier", example = "123456789")
        Integer contact,

        @NotBlank(message = "The address cannot be blank")
        @Schema(description = "The address of the supplier", example = "Calle 123")
        String address,

        @NotNull(message = "The category cannot be null")
        @Schema(description = "The category that the supplier have", example = "PORTATILES")
        Category category,

        @Schema(description = "The deleted status of the supplier", example = "false")
        Boolean isDeleted
) {
}
