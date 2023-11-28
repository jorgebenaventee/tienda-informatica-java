package dev.clownsinformatics.tiendajava.rest.products.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Clase encargada de realizar la conversión entre objetos DTO y modelos de productos.
 * Utiliza el patrón de diseño Mapper para facilitar la transformación de datos.
 *
 * @version 1.0
 * @since 2023-11-28
 */
@Component
public class ProductMapper {
    /**
     * Convierte un objeto {@link ProductCreateDto} a un objeto {@link Product}.
     *
     * @param productCreateDto DTO con la información del nuevo producto.
     * @param category         Categoría del producto.
     * @param supplier         Proveedor del producto.
     * @return Objeto {@link Product} creado a partir del DTO.
     */
    public Product toProduct(ProductCreateDto productCreateDto, Category category, Supplier supplier) {
        return Product.builder()
                .name(productCreateDto.name())
                .weight(productCreateDto.weight())
                .price(productCreateDto.price())
                .img(productCreateDto.img())
                .stock(productCreateDto.stock())
                .description(productCreateDto.description())
                .category(category)
                .supplier(supplier)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte un objeto {@link ProductUpdateDto} a un objeto {@link Product} actualizado.
     *
     * @param productUpdateDto DTO con la información de actualización del producto.
     * @param product          Producto existente que se actualizará.
     * @param category         Nueva categoría del producto.
     * @param supplier         Nuevo proveedor del producto.
     * @return Objeto {@link Product} actualizado a partir del DTO y el producto existente.
     */
    public Product toProduct(ProductUpdateDto productUpdateDto, Product product, Category category, Supplier supplier) {
        return Product.builder()
                .id(product.getId())
                .name(productUpdateDto.name() != null ? productUpdateDto.name() : product.getName())
                .weight(productUpdateDto.weight() != null ? productUpdateDto.weight() : product.getWeight())
                .price(productUpdateDto.price() != null ? productUpdateDto.price() : product.getPrice())
                .img(productUpdateDto.img() != null ? productUpdateDto.img() : product.getImg())
                .stock(productUpdateDto.stock() != null ? productUpdateDto.stock() : product.getStock())
                .description(productUpdateDto.description() != null ? productUpdateDto.description() : product.getDescription())
                .category(category)
                .supplier(supplier)
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte un objeto {@link Product} a un objeto {@link ProductResponseDto}.
     *
     * @param product Producto a convertir en DTO de respuesta.
     * @return Objeto {@link ProductResponseDto} creado a partir del producto.
     */
    public ProductResponseDto toProductResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getWeight(),
                product.getPrice(),
                product.getImg(),
                product.getStock(),
                product.getDescription(),
                product.getCategory(),
                product.getSupplier(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getIsDeleted()
        );
    }
}
