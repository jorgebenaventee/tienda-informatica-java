package dev.clownsinformatics.tiendajava.rest.suppliers.dto;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record SupplierUpdateDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        @Schema(description = "The name of the supplier", example = "Eva")
        String name,

        @Min(value = 0, message = "The contact must be greater than 0")
        @Schema(description = "The contact of the supplier", example = "123456789")
        Integer contact,

        @Length(min = 2, max = 50, message = "The address must be between 2 and 50 characters")
        @Schema(description = "The address of the supplier", example = "Calle 123")
        String address,

        @Schema(description = "The category that the supplier have", example = "PORTATILES")
        Category category,

        @Schema(description = "The status of the supplier", example = "false")
        Boolean isDeleted
) {
}
