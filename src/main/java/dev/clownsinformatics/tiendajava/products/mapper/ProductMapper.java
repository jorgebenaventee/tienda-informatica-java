package dev.clownsinformatics.tiendajava.products.mapper;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.models.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductMapper {
    public Product toProduct(Long id, ProductCreateDto productCreateDto) {
        return Product.builder()
                .id(id)
                .uuid(UUID.randomUUID())
                .name(productCreateDto.name())
                .weight(productCreateDto.weight())
                .category(productCreateDto.category())
                .price(productCreateDto.price())
                .idCategory(productCreateDto.idCategory())
                .img(productCreateDto.img())
                .stock(productCreateDto.stock())
                .description(productCreateDto.description())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product toProduct(ProductUpdateDto productUpdateDto, Product product) {
        return Product.builder()
                .id(product.getId())
                .uuid(product.getUuid())
                .name(productUpdateDto.name() != null ? productUpdateDto.name() : product.getName())
                .weight(productUpdateDto.weight() != null ? productUpdateDto.weight() : product.getWeight())
                .category(productUpdateDto.category() != null ? productUpdateDto.category() : product.getCategory())
                .price(productUpdateDto.price() != null ? productUpdateDto.price() : product.getPrice())
                .idCategory(productUpdateDto.idCategory() != null ? productUpdateDto.idCategory() : product.getIdCategory())
                .img(productUpdateDto.img() != null ? productUpdateDto.img() : product.getImg())
                .stock(productUpdateDto.stock() != null ? productUpdateDto.stock() : product.getStock())
                .description(productUpdateDto.description() != null ? productUpdateDto.description() : product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ProductResponseDto toProductoResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getUuid(),
                product.getName(),
                product.getWeight(),
                product.getCategory(),
                product.getPrice(),
                product.getIdCategory(),
                product.getImg(),
                product.getStock(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
