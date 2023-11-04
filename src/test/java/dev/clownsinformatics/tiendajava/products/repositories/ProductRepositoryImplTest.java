package dev.clownsinformatics.tiendajava.products.repositories;

import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryImplTest {
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

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(product1);
        repository.save(product2);
    }

    @Test
    void findAll() {
        List<Product> products = repository.findAll();
        assertAll(
                () -> assertEquals(2, products.size()),
                () -> assertEquals(product1, products.get(0)),
                () -> assertEquals(product2, products.get(1))
        );
    }

    @Test
    void findAllByWeight() {
        List<Product> products = repository.findAllByWeight(3.2);
        assertAll(
                () -> assertEquals(1, products.size()),
                () -> assertEquals(product2, products.get(0))
        );
    }

    @Test
    void findAllByName() {
        List<Product> products = repository.findAllByName("Product 1");
        assertAll(
                () -> assertEquals(1, products.size()),
                () -> assertEquals(product1, products.get(0))
        );
    }

    @Test
    void findAllByNameAndWeight() {
        List<Product> products = repository.findAllByNameAndWeight("Product 1", 2.5);
        assertAll(
                () -> assertEquals(1, products.size()),
                () -> assertEquals(product1, products.get(0))
        );
    }

    @Test
    void findById() {
        Optional<Product> product = repository.findById(idProduct1);
        assertAll(
                () -> assertTrue(product.isPresent()),
                () -> assertEquals(product1.getId(), product.get().getId())
        );
    }

    @Test
    void findByIdNotFound() {
        Optional<Product> product = repository.findById(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460"));
        assertAll(
                () -> assertTrue(product.isEmpty()),
                () -> assertNotNull(product)
        );
    }

    @Test
    void findByIdCategory() {
        Optional<Product> products = repository.findByIdCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"));
        assertAll(
                () -> assertTrue(products.isPresent()),
                () -> assertEquals(product1.getIdCategory(), products.get().getIdCategory())
        );
    }

    @Test
    void findByIdCategoryNotFound() {
        Optional<Product> product = repository.findByIdCategory(UUID.fromString("cdf6ff32-181e-4006-9f4f-694e00785461"));
        assertAll(
                () -> assertTrue(product.isEmpty()),
                () -> assertNotNull(product)
        );
    }

    @Test
    void save() {
        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        Product product = Product.builder()
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
        repository.save(product);
        assertAll(
                () -> assertEquals(3, repository.findAll().size()),
                () -> assertEquals(product, repository.products.get(id))
        );
    }

    @Test
    void deleteById() {
        repository.deleteById(idProduct1);
        assertAll(
                () -> assertEquals(1, repository.findAll().size()),
                () -> assertNull(repository.products.get(idProduct1))
        );
    }

    @Test
    void deleteByIdCategory() {
        repository.deleteByIdCategory(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785467"));
        assertAll(
                () -> assertEquals(1, repository.findAll().size())
        );
    }

    @Test
    void deleteAll() {
        repository.deleteAll();
        assertAll(
                () -> assertEquals(0, repository.findAll().size())
        );
    }

    @Test
    void getRandomUUID() {
        UUID uuid = repository.getRandomUUID();
        assertNotNull(uuid);
    }
}