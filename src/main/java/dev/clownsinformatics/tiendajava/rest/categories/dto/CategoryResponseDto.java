package dev.clownsinformatics.tiendajava.rest.categories.dto;

import org.hibernate.validator.constraints.Length;

public record CategoryResponseDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,
        Boolean isDeleted
) {
}
