package dev.clownsinformatics.tiendajava.rest.products.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductBadRequest;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.mapper.ProductMapper;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.repositories.ProductRepository;
import dev.clownsinformatics.tiendajava.rest.storage.services.StorageService;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.suppliers.services.SupplierService;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ProductNotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

    private final Supplier supplier1 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category1)
            .build();


    private final Supplier supplier2 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category2)
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

    private final ProductResponseDto productResponseDto1 = new ProductResponseDto(idProduct1, "Product 1", 2.5, 50.0, "imagen1.jpg", 10, "Descripcion del producto 1", category1, supplier1, LocalDateTime.now(), LocalDateTime.now(), false);
    private final ProductResponseDto productResponseDto2 = new ProductResponseDto(idProduct2, "Product 2", 3.2, 50.0, "imagen2.jpg", 10, "Descripcion del producto 2", category2, supplier2, LocalDateTime.now(), LocalDateTime.now(), false);

    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private CategoryService categoryService;
    @Mock
    private StorageService storageService;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private ProductNotificationMapper productNotificationMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private SupplierMapper supplierMapper;
    @Mock
    private SupplierService supplierService;
    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void findAll() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Collections.singletonList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Collections.singletonList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        Optional<Boolean> isDeleted = Optional.empty();

        List<Product> expectedProducts = Collections.singletonList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByIsDeleted() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        Optional<Boolean> isDeleted = Optional.of(true);

        List<Product> expectedProducts = Collections.singletonList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void findAllByCategoryAllParams() {
        Optional<String> name = Optional.of("Product 1");
        Optional<Double> maxWeight = Optional.of(2.5);
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.of(10.0);
        Optional<String> category = Optional.of("Category 1");
        Optional<Boolean> isDeleted = Optional.of(false);

        List<Product> expectedProducts = Collections.singletonList(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);

        Page<ProductResponseDto> actualPage = service.findAll(name, maxWeight, maxPrice, minStock, category, isDeleted, pageable);

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
        when(mapper.toProductResponseDto(product1)).thenReturn(productResponseDto1);

        ProductResponseDto actualProduct = service.findById(idProduct1.toString());
        assertAll(
                () -> assertEquals(idProduct1, actualProduct.id()),
                () -> assertEquals("Product 1", actualProduct.name()),
                () -> assertEquals(2.5, actualProduct.weight()),
                () -> assertEquals(50.0, actualProduct.price()),
                () -> assertEquals("imagen1.jpg", actualProduct.img()),
                () -> assertEquals(10, actualProduct.stock()),
                () -> assertEquals("Descripcion del producto 1", actualProduct.description()),
                () -> assertNotNull(actualProduct.createdAt()),
                () -> assertNotNull(actualProduct.updatedAt()),
                () -> assertEquals(category1, actualProduct.category())
        );
        verify(repository, times(1)).findById(idProduct1);
        verify(mapper, times(1)).toProductResponseDto(product1);
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
    void save() throws IOException {
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1,
                supplier1
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
                .supplier(supplier1)
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
                supplier1,
                productExpected.getCreatedAt(),
                productExpected.getUpdatedAt(),
                false
        );

        SupplierResponseDto supplierResponseDto = new SupplierResponseDto(
                supplier1.getId(),
                supplier1.getName(),
                supplier1.getContact(),
                supplier1.getAddress(),
                supplier1.getDateOfHire(),
                supplier1.getCategory()
        );

        when(categoryService.findByName(productCreateDto.category().getName())).thenReturn(category1);
        when(mapper.toProduct(productCreateDto, category1, supplier1)).thenReturn(productExpected);
        when(supplierMapper.toSupplier(supplierResponseDto)).thenReturn(supplier1);
        when(supplierService.findByName(anyString())).thenReturn(supplierResponseDto);
        when(repository.save(productExpected)).thenReturn(productExpected);
        when(mapper.toProductResponseDto(productExpected)).thenReturn(productResponseDto);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

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
        verify(categoryService, times(1)).findByName(productCreateDto.category().getName());
        verify(repository, times(1)).save(productExpected);
        verify(mapper, times(1)).toProduct(productCreateDto, category1, supplier1);
        verify(mapper, times(1)).toProductResponseDto(productExpected);
    }

    @Test
    void update() throws IOException {
        UUID id = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785460");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 3",
                2.5,
                50.0,
                "imagen3.jpg",
                10,
                "Descripción del producto 3",
                category1,
                supplier1
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
                .supplier(supplier1)
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
                supplier1,
                productExpected.getCreatedAt(),
                productExpected.getUpdatedAt(),
                false
        );

        SupplierResponseDto supplierResponseDto = new SupplierResponseDto(
                supplier1.getId(),
                supplier1.getName(),
                supplier1.getContact(),
                supplier1.getAddress(),
                supplier1.getDateOfHire(),
                supplier1.getCategory()
        );

        when(repository.findById(id)).thenReturn(Optional.of(productExpected));
        when(mapper.toProduct(productUpdateDto, productExpected, category1, supplier1)).thenReturn(productExpected);
        when(supplierMapper.toSupplier(supplierResponseDto)).thenReturn(supplier1);
        when(supplierService.findByName(anyString())).thenReturn(supplierResponseDto);
        when(repository.save(productExpected)).thenReturn(productExpected);
        when(mapper.toProductResponseDto(productExpected)).thenReturn(productResponseDto);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

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
        verify(repository, times(1)).save(productExpected);
        verify(mapper, times(1)).toProduct(productUpdateDto, productExpected, category1, supplier1);
        verify(mapper, times(1)).toProductResponseDto(productExpected);
    }

    @Test
    void updateImage() throws IOException {
        String imageUrl = "imagen1.jpg";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(repository.findById(idProduct1)).thenReturn(Optional.of(product1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when(storageService.getUrl(imageUrl)).thenReturn(imageUrl);
        when(repository.save(any(Product.class))).thenReturn(product1);
        when(mapper.toProductResponseDto(any(Product.class))).thenReturn(productResponseDto1);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        ProductResponseDto updatedProduct = service.updateImage(idProduct1.toString(), multipartFile);

        // Assert
        assertEquals(imageUrl, updatedProduct.img());

        verify(repository, times(1)).save(any(Product.class));
        verify(storageService, times(1)).delete(product1.getImg());
        verify(storageService, times(1)).store(multipartFile);
        verify(storageService, times(1)).getUrl(imageUrl);
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