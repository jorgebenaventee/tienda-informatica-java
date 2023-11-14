package dev.clownsinformatics.tiendajava.rest.categories.repositories;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private final UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private final Category category = Category.builder()
            .uuid(uuid)
            .name("DISNEY")
            .build();

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.merge(category);
        entityManager.flush();
    }

    @Test
    void findByNameEqualsIgnoreCase() {
        Optional<Category> categories = categoryRepository.findByNameEqualsIgnoreCase("DISNEY");
        assertAll(
                () -> assertTrue(categories.isPresent())
        );
    }

    @Test
    void findByUuid() {
        Optional<Category> result = categoryRepository.findByUuid(category.getUuid());
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(category.getUuid(), result.get().getUuid())
        );
    }

    @Test
    void findByName() {
        Optional<Category> categories = categoryRepository.findByName("DISNEY");
        assertAll(
                () -> assertTrue(categories.isPresent()),
                () -> assertEquals("DISNEY", categories.get().getName())
        );
    }

    @Test
    void existsProductById() {
        UUID uuidProduct = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        Product product = Product.builder()
                .id(uuidProduct)
                .name("Product 1")
                .weight(100.0)
                .price(10.0)
                .stock(10)
                .category(category)
                .description("Description")
                .build();
        entityManager.merge(product);
        entityManager.flush();

        Boolean exists = categoryRepository.existsProductById(category.getUuid());
        assertAll(
                () -> assertNotNull(exists),
                () -> assertTrue(exists)
        );
    }

    @Test
    void existsProveedorById() {
        Boolean exists = categoryRepository.existsProveedorById(uuid);
        assertAll(
                () -> assertNotNull(exists),
                () -> assertFalse(exists)
        );
    }
}