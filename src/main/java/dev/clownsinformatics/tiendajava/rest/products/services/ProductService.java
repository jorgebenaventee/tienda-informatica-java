package dev.clownsinformatics.tiendajava.rest.products.services;

import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    UUID getUUID(String id);

    Page<ProductResponseDto> findAll(Optional<String> name, Optional<Double> maxWeight, Optional<Double> maxPrice, Optional<Double> minStock, Optional<String> category, Optional<Boolean> isDeleted, Pageable pageable);

    ProductResponseDto findById(String id);

    ProductResponseDto save(ProductCreateDto productCreateDto);

    ProductResponseDto update(String id, ProductUpdateDto productUpdateDto);

    ProductResponseDto updateImage(String id, MultipartFile image);

    void deleteById(String id);
}
