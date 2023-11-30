package dev.clownsinformatics.tiendajava.rest.suppliers.dto;


import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierResponseDto(
        @Schema(description = "The id of the supplier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "The name of the supplier", example = "Eva")
        String name,

        @Schema(description = "The contact of the supplier", example = "123456789")
        Integer contact,

        @Schema(description = "The address of the supplier", example = "Calle 123")
        String address,

        @Schema(description = "The date of hire of the supplier", example = "2021-10-10T10:10:10")
        LocalDateTime dateOfHire,

        @Schema(description = "The category that the supplier have", example = "PORTATILES")
        Category category,

        @Schema(description = "The deleted status of the supplier", example = "false")
        Boolean isDeleted
) {
}
