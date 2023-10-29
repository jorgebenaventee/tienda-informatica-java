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
    public Product toProduct(UUID id, ProductCreateDto productCreateDto) {
        return Product.builder()
                .id(id)
                .name(productCreateDto.name())
                .weight(productCreateDto.weight())
                .idCategory(productCreateDto.idCategory() != null ? productCreateDto.idCategory() : UUID.randomUUID())
                .price(productCreateDto.price())
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
                .name(productUpdateDto.name() != null ? productUpdateDto.name() : product.getName())
                .weight(productUpdateDto.weight() != null ? productUpdateDto.weight() : product.getWeight())
                .idCategory(productUpdateDto.idCategory() != null ? productUpdateDto.idCategory() : product.getIdCategory())
                .price(productUpdateDto.price() != null ? productUpdateDto.price() : product.getPrice())
                .img(productUpdateDto.img() != null ? productUpdateDto.img() : product.getImg())
                .stock(productUpdateDto.stock() != null ? productUpdateDto.stock() : product.getStock())
                .description(productUpdateDto.description() != null ? productUpdateDto.description() : product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getWeight(),
                product.getIdCategory(),
                product.getPrice(),
                product.getImg(),
                product.getStock(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
