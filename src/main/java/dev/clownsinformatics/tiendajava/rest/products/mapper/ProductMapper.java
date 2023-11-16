package dev.clownsinformatics.tiendajava.rest.products.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {
    public Product toProduct(ProductCreateDto productCreateDto, Category category) {
        return Product.builder()
                .name(productCreateDto.name())
                .weight(productCreateDto.weight())
                .price(productCreateDto.price())
                .img(productCreateDto.img())
                .stock(productCreateDto.stock())
                .description(productCreateDto.description())
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product toProduct(ProductUpdateDto productUpdateDto, Product product, Category category) {
        return Product.builder()
                .id(product.getId())
                .name(productUpdateDto.name() != null ? productUpdateDto.name() : product.getName())
                .weight(productUpdateDto.weight() != null ? productUpdateDto.weight() : product.getWeight())
                .price(productUpdateDto.price() != null ? productUpdateDto.price() : product.getPrice())
                .img(productUpdateDto.img() != null ? productUpdateDto.img() : product.getImg())
                .stock(productUpdateDto.stock() != null ? productUpdateDto.stock() : product.getStock())
                .description(productUpdateDto.description() != null ? productUpdateDto.description() : product.getDescription())
                .category(category)
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

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
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
