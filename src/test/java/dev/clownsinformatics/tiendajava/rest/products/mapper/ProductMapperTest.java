package dev.clownsinformatics.tiendajava.rest.products.mapper;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapper();
    private final Category category1 = Category.builder()
            .uuid(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"))
            .name("Category 1")
            .build();

    @Test
    void toProduct() {
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1

        );
        var res = mapper.toProduct(productCreateDto, category1);
        assertAll(
                () -> assertEquals(productCreateDto.name(), res.getName()),
                () -> assertEquals(productCreateDto.price(), res.getPrice()),
                () -> assertEquals(productCreateDto.price(), res.getPrice()),
                () -> assertEquals(productCreateDto.img(), res.getImg()),
                () -> assertEquals(productCreateDto.stock(), res.getStock()),
                () -> assertEquals(productCreateDto.description(), res.getDescription()),
                () -> assertEquals(category1, res.getCategory())
        );
    }

    @Test
    void testToProduct() {
        UUID uuid = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1
        );
        Product product = Product.builder()
                .id(uuid)
                .name(productUpdateDto.name())
                .price(productUpdateDto.price())
                .price(productUpdateDto.price())
                .img(productUpdateDto.img())
                .stock(productUpdateDto.stock())
                .description(productUpdateDto.description())
                .category(category1)
                .build();
        var res = mapper.toProduct(productUpdateDto, product, category1);
        assertAll(
                () -> assertEquals(uuid, res.getId()),
                () -> assertEquals(productUpdateDto.name(), res.getName()),
                () -> assertEquals(productUpdateDto.price(), res.getPrice()),
                () -> assertEquals(productUpdateDto.price(), res.getPrice()),
                () -> assertEquals(productUpdateDto.img(), res.getImg()),
                () -> assertEquals(productUpdateDto.stock(), res.getStock()),
                () -> assertEquals(productUpdateDto.description(), res.getDescription()),
                () -> assertEquals(category1, res.getCategory())
        );

    }

    @Test
    void toProductResponseDto() {
        UUID uuid = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        Product product = Product.builder()
                .id(uuid)
                .name("Product 3")
                .price(50.0)
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .category(category1)
                .build();
        var res = mapper.toProductResponseDto(product);
        assertAll(
                () -> assertEquals(uuid, res.id()),
                () -> assertEquals(product.getName(), res.name()),
                () -> assertEquals(product.getPrice(), res.price()),
                () -> assertEquals(product.getPrice(), res.price()),
                () -> assertEquals(product.getImg(), res.img()),
                () -> assertEquals(product.getStock(), res.stock()),
                () -> assertEquals(product.getDescription(), res.description()),
                () -> assertEquals(category1, res.category())
        );
    }
}