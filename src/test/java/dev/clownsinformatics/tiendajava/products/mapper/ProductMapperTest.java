package dev.clownsinformatics.tiendajava.products.mapper;

import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapper();

    @Test
    void toProduct() {
        UUID uuid = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "Product 3",
                2.5,
                UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462"),
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3"
        );
        var res = mapper.toProduct(uuid, productCreateDto);
        assertAll(
                () -> assertEquals(uuid, res.getId()),
                () -> assertEquals(productCreateDto.name(), res.getName()),
                () -> assertEquals(productCreateDto.price(), res.getPrice()),
                () -> assertEquals(productCreateDto.idCategory(), res.getIdCategory()),
                () -> assertEquals(productCreateDto.price(), res.getPrice()),
                () -> assertEquals(productCreateDto.img(), res.getImg()),
                () -> assertEquals(productCreateDto.stock(), res.getStock()),
                () -> assertEquals(productCreateDto.description(), res.getDescription())
        );
    }

    @Test
    void testToProduct() {
        UUID uuid = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 3",
                2.5,
                UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462"),
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3"
        );
        Product product = Product.builder()
                .id(uuid)
                .name(productUpdateDto.name())
                .price(productUpdateDto.price())
                .idCategory(productUpdateDto.idCategory())
                .price(productUpdateDto.price())
                .img(productUpdateDto.img())
                .stock(productUpdateDto.stock())
                .description(productUpdateDto.description())
                .build();
        var res = mapper.toProduct(productUpdateDto, product);
        assertAll(
                () -> assertEquals(uuid, res.getId()),
                () -> assertEquals(productUpdateDto.name(), res.getName()),
                () -> assertEquals(productUpdateDto.price(), res.getPrice()),
                () -> assertEquals(productUpdateDto.idCategory(), res.getIdCategory()),
                () -> assertEquals(productUpdateDto.price(), res.getPrice()),
                () -> assertEquals(productUpdateDto.img(), res.getImg()),
                () -> assertEquals(productUpdateDto.stock(), res.getStock()),
                () -> assertEquals(productUpdateDto.description(), res.getDescription())
        );

    }

    @Test
    void toProductResponseDto() {
        UUID uuid = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        Product product = Product.builder()
                .id(uuid)
                .name("Product 3")
                .price(50.0)
                .idCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462"))
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .build();
        var res = mapper.toProductResponseDto(product);
        assertAll(
                () -> assertEquals(uuid, res.id()),
                () -> assertEquals(product.getName(), res.name()),
                () -> assertEquals(product.getPrice(), res.price()),
                () -> assertEquals(product.getIdCategory(), res.idCategory()),
                () -> assertEquals(product.getPrice(), res.price()),
                () -> assertEquals(product.getImg(), res.img()),
                () -> assertEquals(product.getStock(), res.stock()),
                () -> assertEquals(product.getDescription(), res.description())
        );
    }
}