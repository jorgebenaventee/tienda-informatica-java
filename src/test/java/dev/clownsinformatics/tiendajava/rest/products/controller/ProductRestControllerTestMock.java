package dev.clownsinformatics.tiendajava.rest.products.controller;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.services.ProductServiceImpl;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTestMock {
    private final UUID idProduct1 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785464");
    private final UUID idProduct2 = UUID.fromString("cdf61632-181e-4006-9f4f-694e00785462");

    private final Category category1 = Category.builder().uuid(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785461")).name("Category 1").build();
    private final Category category2 = Category.builder().uuid(UUID.fromString("cdf61632-181e-4006-9f4f-694e00785467")).name("Category 2").build();

    private final ProductResponseDto productResponseDto1 = new ProductResponseDto(idProduct1, "Product 1", 2.5, 50.0, "imagen1.jpg", 10, "Descripcion del producto 1", category1, LocalDateTime.now(), LocalDateTime.now());
    private final ProductResponseDto productResponseDto2 = new ProductResponseDto(idProduct2, "Product 2", 3.2, 50.0, "imagen2.jpg", 10, "Descripcion del producto 2", category2, LocalDateTime.now(), LocalDateTime.now());
    @Mock
    PaginationLinksUtils paginationLinksUtils;
    @Mock
    private ProductServiceImpl productService;
    @InjectMocks
    private ProductRestController productRestController;

    @Test
    void getAllProducts() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = Arrays.asList(productResponseDto1, productResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByName() {
        Optional<String> name = Optional.of("Product 1");
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = List.of(productResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByMaxWeight() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.of(2.5);
        Optional<Double> maxPrice = Optional.empty();
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = List.of(productResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByMaxPrice() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = Arrays.asList(productResponseDto1, productResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByMinStock() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.empty();
        Optional<Double> minStock = Optional.of(10.0);
        Optional<String> category = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = Arrays.asList(productResponseDto1, productResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByCategory() {
        Optional<String> name = Optional.empty();
        Optional<Double> maxWeight = Optional.empty();
        Optional<Double> maxPrice = Optional.empty();
        Optional<Double> minStock = Optional.empty();
        Optional<String> category = Optional.of("Category 1");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = List.of(productResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getAllProductsByAllParams() {
        Optional<String> name = Optional.of("Product 1");
        Optional<Double> maxWeight = Optional.of(2.5);
        Optional<Double> maxPrice = Optional.of(50.0);
        Optional<Double> minStock = Optional.of(10.0);
        Optional<String> category = Optional.of("Category 1");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/products"));

        List<ProductResponseDto> expectedProducts = List.of(productResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponseDto> expectedPage = new PageImpl<>(expectedProducts);

        when(productService.findAll(name, maxWeight, maxPrice, minStock, category, pageable)).thenReturn(expectedPage);

        PageResponse<ProductResponseDto> response = productRestController.getAllProducts(name, maxWeight, maxPrice, minStock, category, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(expectedPage.getContent(), response.content())
        );

        verify(productService, times(1)).findAll(name, maxWeight, maxPrice, minStock, category, pageable);
    }

    @Test
    void getProductById() {
        when(productService.findById(idProduct1.toString())).thenReturn(productResponseDto1);
        ResponseEntity<ProductResponseDto> response = productRestController.getProductById(idProduct1.toString());
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(productResponseDto1, response.getBody())
        );
        verify(productService, times(1)).findById(idProduct1.toString());
    }

    @Test
    void postProduct() {
        ProductCreateDto productCreateDto = new ProductCreateDto("Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1);
        ProductResponseDto productResponseDto = new ProductResponseDto(UUID.randomUUID(), "Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1, LocalDateTime.now(), LocalDateTime.now());
        when(productService.save(any(ProductCreateDto.class))).thenReturn(productResponseDto);
        ResponseEntity<ProductResponseDto> response = productRestController.postProduct(productCreateDto);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(productCreateDto.name(), response.getBody().name()),
                () -> assertEquals(productCreateDto.weight(), response.getBody().weight()),
                () -> assertEquals(productCreateDto.price(), response.getBody().price()),
                () -> assertEquals(productCreateDto.img(), response.getBody().img()),
                () -> assertEquals(productCreateDto.stock(), response.getBody().stock()),
                () -> assertEquals(productCreateDto.description(), response.getBody().description()),
                () -> assertEquals(productCreateDto.category(), response.getBody().category())
        );
        verify(productService, times(1)).save(productCreateDto);
    }

    @Test
    void putProduct() {
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1);
        ProductResponseDto productResponseDto = new ProductResponseDto(idProduct1, "Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1, LocalDateTime.now(), LocalDateTime.now());
        when(productService.update(anyString(), any(ProductUpdateDto.class))).thenReturn(productResponseDto);
        ResponseEntity<ProductResponseDto> response = productRestController.putProduct(idProduct1.toString(), productUpdateDto);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(productUpdateDto.name(), response.getBody().name()),
                () -> assertEquals(productUpdateDto.weight(), response.getBody().weight()),
                () -> assertEquals(productUpdateDto.price(), response.getBody().price()),
                () -> assertEquals(productUpdateDto.img(), response.getBody().img()),
                () -> assertEquals(productUpdateDto.stock(), response.getBody().stock()),
                () -> assertEquals(productUpdateDto.description(), response.getBody().description()),
                () -> assertEquals(productUpdateDto.category(), response.getBody().category())
        );
        verify(productService, times(1)).update(idProduct1.toString(), productUpdateDto);
    }

    @Test
    void patchProduct() {
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1);
        ProductResponseDto productResponseDto = new ProductResponseDto(idProduct1, "Product 3", 2.5, 50.0, "imagen3.jpg", 10, "Descripcion del producto 3", category1, LocalDateTime.now(), LocalDateTime.now());
        when(productService.update(anyString(), any(ProductUpdateDto.class))).thenReturn(productResponseDto);
        ResponseEntity<ProductResponseDto> response = productRestController.patchProduct(idProduct1.toString(), productUpdateDto);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(productUpdateDto.name(), response.getBody().name()),
                () -> assertEquals(productUpdateDto.weight(), response.getBody().weight()),
                () -> assertEquals(productUpdateDto.price(), response.getBody().price()),
                () -> assertEquals(productUpdateDto.img(), response.getBody().img()),
                () -> assertEquals(productUpdateDto.stock(), response.getBody().stock()),
                () -> assertEquals(productUpdateDto.description(), response.getBody().description()),
                () -> assertEquals(productUpdateDto.category(), response.getBody().category())
        );
        verify(productService, times(1)).update(idProduct1.toString(), productUpdateDto);
    }

    @Test
    void patchProductImage() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "imagen3.jpg", "image/jpeg", "imagen3.jpg".getBytes());
        ProductResponseDto productResponseDto = new ProductResponseDto(idProduct1, "Product 1", 2.5, 50.0, "imagen1.jpg", 10, "Descripcion del producto 1", category1, LocalDateTime.now(), LocalDateTime.now());
        when(productService.updateImage(anyString(), eq(mockFile))).thenReturn(productResponseDto);
        ResponseEntity<ProductResponseDto> response = productRestController.patchProductImage(idProduct1.toString(), mockFile);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(productResponseDto.name(), response.getBody().name()),
                () -> assertEquals(productResponseDto.weight(), response.getBody().weight()),
                () -> assertEquals(productResponseDto.price(), response.getBody().price()),
                () -> assertEquals(productResponseDto.img(), response.getBody().img()),
                () -> assertEquals(productResponseDto.stock(), response.getBody().stock()),
                () -> assertEquals(productResponseDto.description(), response.getBody().description()),
                () -> assertEquals(productResponseDto.category(), response.getBody().category())
        );
        verify(productService, times(1)).updateImage(idProduct1.toString(), mockFile);
    }

    @Test
    void deleteProduct() {
        ResponseEntity<Void> response = productRestController.deleteProduct(idProduct1.toString());
        assertAll(
                () -> assertNotNull(response),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );
        verify(productService, times(1)).deleteById(idProduct1.toString());
    }
}