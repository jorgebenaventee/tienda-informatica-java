package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.products.models.Product;
import dev.clownsinformatics.tiendajava.products.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private final UUID idProduct1 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785464");
    private final UUID idProduct2 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462");

    private final Product product1 = Product.builder()
            .id(idProduct1)
            .name("Product 1")
            .weight(2.5)
            .idCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"))
            .price(50.0)
            .img("imagen1.jpg")
            .stock(10)
            .description("Descripción del producto 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Product product2 = Product.builder()
            .id(idProduct2)
            .name("Product 2")
            .weight(3.2)
            .idCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785467"))
            .price(50.0)
            .img("imagen2.jpg")
            .stock(10)
            .description("Descripción del producto 2")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @InjectMocks
    private ProductServiceImpl service;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void findAll() {
        List<Product> products = List.of(product1, product2);
        when(repository.findAll()).thenReturn(products);
        List<Product> actualProducts = service.findAll(null, null);
        assertIterableEquals(products, actualProducts);
    }

    @Test
    void findAllByName() {
        when(repository.findAllByName("Product 2")).thenReturn(List.of(product2));
        List<Product> actualProducts = service.findAll(null, "Product 2");
        assertAll(
                () -> assertEquals(1, actualProducts.size()),
                () -> assertEquals(product2, actualProducts.get(0))
        );
        verify(repository, times(1)).findAllByName("Product 2");
    }

    @Test
    void findAllByWeight() {
        when(repository.findAllByWeight(3.2)).thenReturn(List.of(product2));
        List<Product> actualProducts = service.findAll(3.2, null);
        assertAll(
                () -> assertEquals(1, actualProducts.size()),
                () -> assertEquals(product2, actualProducts.get(0))
        );
        verify(repository, times(1)).findAllByWeight(3.2);
    }

    @Test
    void findAllByNameAndWeight() {
        when(repository.findAllByNameAndWeight("Product 2", 3.2)).thenReturn(List.of(product2));
        List<Product> actualProducts = service.findAll(3.2, "Product 2");
        assertAll(
                () -> assertEquals(1, actualProducts.size()),
                () -> assertEquals(product2, actualProducts.get(0))
        );
        verify(repository, times(1)).findAllByNameAndWeight("Product 2", 3.2);
    }

    @Test
    void findById() {
        when(repository.findById(idProduct1)).thenReturn(Optional.of(product1));
        Product actualProduct = service.findById(idProduct1.toString());
        assertAll(
                () -> assertEquals(idProduct1, actualProduct.getId()),
                () -> assertEquals("Product 1", actualProduct.getName()),
                () -> assertEquals(2.5, actualProduct.getWeight()),
                () -> assertEquals(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"), actualProduct.getIdCategory()),
                () -> assertEquals(50.0, actualProduct.getPrice()),
                () -> assertEquals("imagen1.jpg", actualProduct.getImg()),
                () -> assertEquals(10, actualProduct.getStock()),
                () -> assertEquals("Descripción del producto 1", actualProduct.getDescription()),
                () -> assertNotNull(actualProduct.getCreatedAt()),
                () -> assertNotNull(actualProduct.getUpdatedAt())
        );
        verify(repository, times(1)).findById(idProduct1);
    }

    @Test
    void findByIdNotFound(){
        when(repository.findById(idProduct1)).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.findById(idProduct1.toString()));
        verify(repository, times(1)).findById(idProduct1);
    }

    @Test
    void findByIdCategory() {
        UUID idCategory = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461");
        when(repository.findByIdCategory(idCategory)).thenReturn(Optional.of(product1));
        Product actualProducts = service.findByIdCategory(idCategory.toString());
        assertAll(
                () -> assertEquals(idProduct1, actualProducts.getId()),
                () -> assertEquals("Product 1", actualProducts.getName()),
                () -> assertEquals(2.5, actualProducts.getWeight()),
                () -> assertEquals(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"), actualProducts.getIdCategory()),
                () -> assertEquals(50.0, actualProducts.getPrice()),
                () -> assertEquals("imagen1.jpg", actualProducts.getImg()),
                () -> assertEquals(10, actualProducts.getStock()),
                () -> assertEquals("Descripción del producto 1", actualProducts.getDescription()),
                () -> assertNotNull(actualProducts.getCreatedAt()),
                () -> assertNotNull(actualProducts.getUpdatedAt())
        );
        verify(repository, times(1)).findByIdCategory(idCategory);
    }

    @Test
    void save() {
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "Product 3",
                2.5,
                UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"),
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3"
        );
        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        Product productExpected = Product.builder()
                .id(id)
                .name("Product 3")
                .weight(2.5)
                .idCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"))
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.getRandomUUID()).thenReturn(id);
        when(mapper.toProduct(id, productCreateDto)).thenReturn(productExpected);
        when(repository.save(productExpected)).thenReturn(productExpected);

        Product actualProduct = service.save(productCreateDto);
        assertAll(
                () -> assertEquals(id, actualProduct.getId()),
                () -> assertEquals("Product 3", actualProduct.getName()),
                () -> assertEquals(2.5, actualProduct.getWeight()),
                () -> assertEquals(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"), actualProduct.getIdCategory()),
                () -> assertEquals(50.0, actualProduct.getPrice()),
                () -> assertEquals("imagen3.jpg", actualProduct.getImg()),
                () -> assertEquals(10, actualProduct.getStock()),
                () -> assertEquals("Descripción del producto 3", actualProduct.getDescription()),
                () -> assertNotNull(actualProduct.getCreatedAt()),
                () -> assertNotNull(actualProduct.getUpdatedAt())
        );
        verify(repository, times(1)).getRandomUUID();
        verify(repository, times(1)).save(productCaptor.capture());
        verify(mapper, times(1)).toProduct(id, productCreateDto);
    }

    @Test
    void update() {
        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 3",
                2.5,
                UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"),
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3"
        );
        Product productExpected = Product.builder()
                .id(id)
                .name("Product 3")
                .weight(2.5)
                .idCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"))
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(productExpected));
        when(mapper.toProduct(productUpdateDto, productExpected)).thenReturn(productExpected);
        when(repository.save(productExpected)).thenReturn(productExpected);

        Product actualProduct = service.update(id.toString(), productUpdateDto);
        assertAll(
                () -> assertEquals(id, actualProduct.getId()),
                () -> assertEquals("Product 3", actualProduct.getName()),
                () -> assertEquals(2.5, actualProduct.getWeight()),
                () -> assertEquals(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"), actualProduct.getIdCategory()),
                () -> assertEquals(50.0, actualProduct.getPrice()),
                () -> assertEquals("imagen3.jpg", actualProduct.getImg()),
                () -> assertEquals(10, actualProduct.getStock()),
                () -> assertEquals("Descripción del producto 3", actualProduct.getDescription()),
                () -> assertNotNull(actualProduct.getCreatedAt()),
                () -> assertNotNull(actualProduct.getUpdatedAt())
        );
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(productCaptor.capture());
        verify(mapper, times(1)).toProduct(productUpdateDto, productExpected);
    }

    @Test
    void deleteById() {
        when(repository.findById(idProduct1)).thenReturn(Optional.of(product1));
        service.deleteById(idProduct1.toString());
        verify(repository, times(1)).findById(idProduct1);
        verify(repository, times(1)).deleteById(idProduct1);
    }

    @Test
    void deleteByIdNotFound() {
        when(repository.findById(idProduct1)).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.deleteById(idProduct1.toString()));
        verify(repository, times(1)).findById(idProduct1);
        verify(repository, times(0)).deleteById(idProduct1);
    }
}