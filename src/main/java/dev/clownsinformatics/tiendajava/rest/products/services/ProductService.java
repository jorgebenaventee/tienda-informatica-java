package dev.clownsinformatics.tiendajava.rest.products.services;

import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> findAll(Double weight, String name);

    Product findById(String id);

    ProductResponseDto save(ProductCreateDto productCreateDto);

    ProductResponseDto update(String id, ProductUpdateDto productUpdateDto);

    Product updateImage(String id, MultipartFile image);

    void deleteById(String id);
}
