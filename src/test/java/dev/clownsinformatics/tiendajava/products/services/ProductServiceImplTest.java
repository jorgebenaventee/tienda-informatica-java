package dev.clownsinformatics.tiendajava.products.services;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadRequest;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import dev.clownsinformatics.tiendajava.rest.products.services.ProductServiceImpl;
import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
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

    private final ProductResponseDto productResponseDto1 = new ProductResponseDto(idProduct1, "Product 1", 2.5, 50.0, "imagen1.jpg", 10, "Descripcion del producto 1", category1, LocalDateTime.now(), LocalDateTime.now());
    private final ProductResponseDto productResponseDto2 = new ProductResponseDto(idProduct2, "Product 2", 3.2, 50.0, "imagen2.jpg", 10, "Descripcion del producto 2", category2, LocalDateTime.now(), LocalDateTime.now());

    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private StorageService storageService;
    @InjectMocks
    private ProductServiceImpl service;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void findAll() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(2)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByName() {
        Optional<String> name = Optional.of("Product 1");
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByWeight() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.of(2.5);
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByPrice() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(2)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByStock() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.of(10.0);
        Optional<String> category = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(2)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByCategory() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.of("Category 1");

        List<Product> expectedProducts = Arrays.asList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByCategoryAllParams(){
        Optional<String> name = Optional.of("Product 1");
        Optional<Double> maxWeight = Optional.of(2.5);
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.of(10.0);
        Optional<String> category = Optional.of("Category 1");

        List<Product> expectedProducts = Arrays.asList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findById() {
        when(repository.findById(idProduct1)).thenReturn(Optional.of(product1));
        Product actualProduct = service.findById(idProduct1.toString());
        assertAll(
                () -> assertEquals(idProduct1, actualProduct.getId()),
                () -> assertEquals("Product 1", actualProduct.getName()),
                () -> assertEquals(2.5, actualProduct.getWeight()),
                () -> assertEquals(50.0, actualProduct.getPrice()),
                () -> assertEquals("imagen1.jpg", actualProduct.getImg()),
                () -> assertEquals(10, actualProduct.getStock()),
                () -> assertEquals("Descripción del producto 1", actualProduct.getDescription()),
                () -> assertNotNull(actualProduct.getCreatedAt()),
                () -> assertNotNull(actualProduct.getUpdatedAt()),
                () -> assertEquals(category1, actualProduct.getCategory())
        );
        verify(repository, times(1)).findById(idProduct1);
    }

    @Test
    void findByIdNotFound() {
        when(repository.findById(idProduct1)).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.findById(idProduct1.toString()));
        verify(repository, times(1)).findById(idProduct1);
    }

    @Test
    void getUUID() {
        UUID uuid = service.getUUID(idProduct1.toString());
        assertEquals(idProduct1, uuid);
    }

    @Test
    void getUUIDBadRequest() {
        assertThrows(ProductBadRequest.class, () -> service.getUUID("cdf61632-181e-4006-9f4f-694e0078546gggssdf"));
    }

    @Test
    void save() {
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1
        );

        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        Product productExpected = Product.builder()
                .id(id)
                .name("Product 3")
                .weight(2.5)
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .category(category1)
                .build();
        ProductResponseDto productResponseDto = new ProductResponseDto(
                id,
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1,
                productExpected.getCreatedAt(),
                productExpected.getUpdatedAt()
        );

        when(categoryRepository.findByNameContainingIgnoreCase("Category 1")).thenReturn(Optional.of(category1));
        when(mapper.toProduct(productCreateDto, category1)).thenReturn(productExpected);
        when(repository.save(productExpected)).thenReturn(productExpected);
        when(mapper.toProductResponseDto(productExpected)).thenReturn(productResponseDto);

        ProductResponseDto actualProduct = service.save(productCreateDto);
        assertAll(
                () -> assertEquals(id, actualProduct.id()),
                () -> assertEquals("Product 3", actualProduct.name()),
                () -> assertEquals(2.5, actualProduct.weight()),
                () -> assertEquals(50.0, actualProduct.price()),
                () -> assertEquals("imagen3.jpg", actualProduct.img()),
                () -> assertEquals(10, actualProduct.stock()),
                () -> assertEquals("Descripción del producto 3", actualProduct.description()),
                () -> assertNotNull(actualProduct.createdAt()),
                () -> assertNotNull(actualProduct.updatedAt()),
                () -> assertEquals(category1, actualProduct.category())
        );
        verify(categoryRepository, times(1)).findByNameContainingIgnoreCase("Category 1");
        verify(repository, times(1)).save(productCaptor.capture());
        verify(mapper, times(1)).toProduct(productCreateDto, category1);
        verify(mapper, times(1)).toProductResponseDto(productExpected);
    }

    @Test
    void update() {
        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1
        );
        Product productExpected = Product.builder()
                .id(id)
                .name("Product 3")
                .weight(2.5)
                .price(50.0)
                .img("imagen3.jpg")
                .stock(10)
                .description("Descripción del producto 3")
                .category(category1)
                .build();
        ProductResponseDto productResponseDto = new ProductResponseDto(
                id,
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1,
                productExpected.getCreatedAt(),
                productExpected.getUpdatedAt()
        );

        when(repository.findById(id)).thenReturn(Optional.of(productExpected));
        when(mapper.toProduct(productUpdateDto, productExpected, category1)).thenReturn(productExpected);
        when(repository.save(productExpected)).thenReturn(productExpected);
        when(mapper.toProductResponseDto(productExpected)).thenReturn(productResponseDto);

        ProductResponseDto actualProduct = service.update(id.toString(), productUpdateDto);
        assertAll(
                () -> assertEquals(id, actualProduct.id()),
                () -> assertEquals("Product 3", actualProduct.name()),
                () -> assertEquals(2.5, actualProduct.weight()),
                () -> assertEquals(50.0, actualProduct.price()),
                () -> assertEquals("imagen3.jpg", actualProduct.img()),
                () -> assertEquals(10, actualProduct.stock()),
                () -> assertEquals("Descripción del producto 3", actualProduct.description()),
                () -> assertNotNull(actualProduct.createdAt()),
                () -> assertNotNull(actualProduct.updatedAt()),
                () -> assertEquals(category1, actualProduct.category())
        );
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(productCaptor.capture());
        verify(mapper, times(1)).toProduct(productUpdateDto, productExpected, category1);
        verify(mapper, times(1)).toProductResponseDto(productExpected);
    }

    @Test
    void updateImage() {
        String imageUrl = "imagen1.jpg";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(repository.findById(idProduct1)).thenReturn(Optional.of(product1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when(storageService.getUrl(imageUrl)).thenReturn(imageUrl);
        when(repository.save(any(Product.class))).thenReturn(product1);

        Product updatedProduct = service.updateImage(idProduct1.toString(), multipartFile);

        // Assert
        assertEquals(imageUrl, updatedProduct.getImg());

        verify(repository, times(1)).save(any(Product.class));
        verify(storageService, times(1)).delete(product1.getImg());
        verify(storageService, times(1)).store(multipartFile);
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