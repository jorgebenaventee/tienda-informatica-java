package dev.clownsinformatics.tiendajava.rest.products.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryImplTest {
    private final UUID idProduct1 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785464");
    private final UUID idProduct2 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462");

    private final Category category1 = Category.builder()
            .uuid(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461"))
            .name("Category 1")
            .build();
    private final Category category2 = Category.builder()
            .uuid(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785467"))
            .name("Category 2")
            .build();

    private final Product product1 = Product.builder()
            .id(idProduct1)
            .name("Product 1")
            .weight(2.5)
            .price(50.0)
            .img("imagen1.jpg")
            .stock(10)
            .description("Descripción del producto 1")
            .category(category1)
            .build();

    private final Product product2 = Product.builder()
            .id(idProduct2)
            .name("Product 2")
            .weight(3.2)
            .price(50.0)
            .img("imagen2.jpg")
            .stock(10)
            .description("Descripción del producto 2")
            .category(category2)
            .build();

    @Autowired
    private ProductRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        entityManager.merge(category1);
        entityManager.merge(category2);
        entityManager.flush();
        entityManager.merge(product1);
        entityManager.merge(product2);
        entityManager.flush();

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
}