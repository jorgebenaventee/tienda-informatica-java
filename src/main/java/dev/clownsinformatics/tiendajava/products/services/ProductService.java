package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.models.Categories;
import dev.clownsinformatics.tiendajava.products.models.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll(Categories category, String name);

    Product findById(Long id);

    Product findByIdCategory(Long idCategory);

    Product findByUUID(UUID uuid);

    Product save(ProductCreateDto productCreateDto);

    Product update(Long id, ProductUpdateDto productUpdateDto);

    void deleteById(Long id);
}
