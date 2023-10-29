package dev.clownsinformatics.tiendajava.products.repositories;

import dev.clownsinformatics.tiendajava.products.models.Categories;
import dev.clownsinformatics.tiendajava.products.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryImplTest {

    private final Product product1 = Product.builder()
            .id(1L)
            .uuid(UUID.randomUUID())
            .name("Product 1")
            .weight(2.5)
            .category(Categories.SOBREMESA)
            .price(50.0)
            .idCategory(1L)
            .img("imagen1.jpg")
            .stock(10)
            .description("Descripción del producto 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Product product2 = Product.builder()
            .id(2L)
            .uuid(UUID.randomUUID())
            .name("Product 2")
            .weight(2.5)
            .category(Categories.SOBREMESA)
            .price(50.0)
            .idCategory(1L)
            .img("imagen2.jpg")
            .stock(10)
            .description("Descripción del producto 2")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private ProductRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProductRepositoryImpl();
        repository.products.clear();
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
    void findAllByCategory() {
        List<Product> products = repository.findAllByCategory(Categories.SOBREMESA);
        assertAll(
                () -> assertEquals(2, products.size()),
                () -> assertEquals(product1, products.get(0)),
                () -> assertEquals(product2, products.get(1))
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
    void findAllByNameAndCategory() {
        List<Product> products = repository.findAllByNameAndCategory("Product 1", Categories.SOBREMESA);
        assertAll(
                () -> assertEquals(1, products.size()),
                () -> assertEquals(product1, products.get(0))
        );
    }

    @Test
    void findById() {
        Optional<Product> product = repository.findById(1L);
        assertAll(
                () -> assertTrue(product.isPresent()),
                () -> assertEquals(product1.getId(), product.get().getId())
        );
    }

    @Test
    void findByIdNotFound(){
        Optional<Product> product = repository.findById(100L);
        assertAll(
                () -> assertTrue(product.isEmpty()),
                () -> assertNotNull(product)
        );
    }

    @Test
    void findByIdCategory() {
        Optional<Product> products = repository.findByIdCategory(1L);
        assertAll(
                () -> assertTrue(products.isPresent()),
                () -> assertEquals(product1.getIdCategory(), products.get().getIdCategory())
        );
    }

    @Test
    void findByIdCategoryNotFound(){
        Optional<Product> product = repository.findByIdCategory(100L);
        assertAll(
                () -> assertTrue(product.isEmpty()),
                () -> assertNotNull(product)
        );
    }

    @Test
    void findByUUID() {
        Optional<Product> products = repository.findByUUID(product1.getUuid());
        assertAll(
                () -> assertTrue(products.isPresent()),
                () -> assertEquals(product1.getUuid(), products.get().getUuid())
        );
    }

    @Test
    void findByUUIDNotFound(){
        Optional<Product> product = repository.findByUUID(UUID.randomUUID());
        assertAll(
                () -> assertTrue(product.isEmpty()),
                () -> assertNotNull(product)
        );
    }

    @Test
    void save() {
        Product product = Product.builder()
                .id(3L)
                .uuid(UUID.randomUUID())
                .name("Product 3")
                .weight(2.5)
                .category(Categories.SOBREMESA)
                .price(50.0)
                .idCategory(1L)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        repository.save(product);
        assertAll(
                () -> assertEquals(3, repository.products.size()),
                () -> assertEquals(product, repository.products.get(3L))
        );
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);
        assertAll(
                () -> assertEquals(1, repository.products.size()),
                () -> assertEquals(product2, repository.products.get(2L))
        );
    }

    @Test
    void deleteByIdCategory() {
        repository.deleteByIdCategory(1L);
        assertAll(
                () -> assertEquals(0, repository.products.size())
        );
    }

    @Test
    void deleteByUUID() {
        repository.deleteByUUID(product1.getUuid());
        assertAll(
                () -> assertEquals(1, repository.products.size()),
                () -> assertEquals(product2, repository.products.get(2L))
        );
    }

    @Test
    void deleteAll() {
        repository.deleteAll();
        assertAll(
                () -> assertEquals(0, repository.products.size())
        );
    }

    @Test
    void nextId() {
        Long id = repository.nextId();
        assertEquals(3L, id);
    }
}