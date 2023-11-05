package dev.clownsinformatics.tiendajava.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductCreateDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductResponseDto;
import dev.clownsinformatics.tiendajava.rest.products.dto.ProductUpdateDto;
import dev.clownsinformatics.tiendajava.rest.products.exceptions.ProductNotFound;
import dev.clownsinformatics.tiendajava.rest.products.models.Product;
import dev.clownsinformatics.tiendajava.rest.products.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private final String BASE_URL = "/api/productos";
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
            .description("Descripcion del producto 1")
            .category(category1)
            .build();

    private final Product product2 = Product.builder()
            .id(idProduct2)
            .name("Product 2")
            .weight(3.2)
            .price(50.0)
            .img("imagen2.jpg")
            .stock(10)
            .description("Descripcion del producto 2")
            .category(category2)
            .build();

    private final ProductResponseDto productResponseDto1 = new ProductResponseDto(
            idProduct1,
            "Product 1",
            2.5,
            50.0,
            "imagen1.jpg",
            10,
            "Descripcion del producto 1",
            category1,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    private final ProductResponseDto productResponseDto2 = new ProductResponseDto(
            idProduct2,
            "Product 2",
            3.2,
            50.0,
            "imagen2.jpg",
            10,
            "Descripcion del producto 2",
            category2,
            LocalDateTime.now(),
            LocalDateTime.now()
    );


    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Autowired
    public ProductRestControllerTest(ProductService productService) {
        this.productService = productService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllProducts() throws Exception {
        List<Product> products = List.of(product1, product2);

        when(productService.findAll(null, null)).thenReturn(products);

        MockHttpServletResponse response = mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Product> productsResponse = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, productsResponse.size()),
                () -> assertEquals(product1.getId(), productsResponse.get(0).getId()),
                () -> assertEquals(product2.getId(), productsResponse.get(1).getId()),
                () -> assertEquals(product1.getName(), productsResponse.get(0).getName()),
                () -> assertEquals(product2.getName(), productsResponse.get(1).getName())

        );
        verify(productService, times(1)).findAll(null, null);
    }

    @Test
    void getAllProductsByWeight() throws Exception {
        Double weight = 2.5;
        var LOCAL_URL = BASE_URL + "?weight=" + weight;
        List<Product> products = List.of(product1);

        when(productService.findAll(weight, null)).thenReturn(products);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Product> productsResponse = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, productsResponse.size()),
                () -> assertEquals(product1.getId(), productsResponse.get(0).getId()),
                () -> assertEquals(product1.getName(), productsResponse.get(0).getName()),
                () -> assertEquals(product1.getWeight(), productsResponse.get(0).getWeight())

        );
        verify(productService, times(1)).findAll(weight, null);
    }

    @Test
    void getAllProductsByName() throws Exception {
        String name = "Product 1";
        var LOCAL_URL = BASE_URL + "?name=" + name;
        List<Product> products = List.of(product1);

        when(productService.findAll(null, name)).thenReturn(products);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Product> productsResponse = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, productsResponse.size()),
                () -> assertEquals(product1.getId(), productsResponse.get(0).getId()),
                () -> assertEquals(product1.getName(), productsResponse.get(0).getName())

        );
        verify(productService, times(1)).findAll(null, name);
    }

    @Test
    void getAllProductsByWeightAndName() throws Exception {
        String name = "Product 1";
        Double weight = 2.5;
        var LOCAL_URL = BASE_URL + "?name=" + name + "&weight=" + weight;
        List<Product> products = List.of(product1);

        when(productService.findAll(weight, name)).thenReturn(products);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Product> productsResponse = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, productsResponse.size()),
                () -> assertEquals(product1.getId(), productsResponse.get(0).getId()),
                () -> assertEquals(product1.getName(), productsResponse.get(0).getName())

        );

        verify(productService, times(1)).findAll(weight, name);
    }

    @Test
    void getProductById() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + idProduct1;
        when(productService.findById(idProduct1.toString())).thenReturn(product1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ProductResponseDto productsResponse = mapper.readValue(response.getContentAsString(),
                ProductResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(product1.getId(), productsResponse.id()),
                () -> assertEquals(product1.getName(), productsResponse.name())
        );
        verify(productService, times(1)).findById(idProduct1.toString());
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + idProduct1;
        when(productService.findById(idProduct1.toString())).thenThrow(new ProductNotFound(idProduct1.toString()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(productService, times(1)).findById(idProduct1.toString());
    }

    @Test
    void postProduct() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        when(productService.save(any(ProductCreateDto.class))).thenReturn(productResponseDto1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ProductResponseDto productResponse = mapper.readValue(response.getContentAsString(), ProductResponseDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(productResponseDto1, productResponse)
        );
        verify(productService, times(1)).save(any(ProductCreateDto.class));
    }

    @Test
    void postProductWithBadRequestNameEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The name cannot be blank"))
        );
    }

    @Test
    void postProductWithBadRequestNameLenght() throws Exception {
        var productDto = new ProductCreateDto(
                "P",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The name must be between 3 and 50 characters"))
        );
    }

    @Test
    void postProductWithBadRequestWeightEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                null,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The weight cannot be null"))
        );
    }

    @Test
    void postProductWithBadRequestWeightMin() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                -2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The weight must be greater than 0"))
        );
    }

    @Test
    void postProductWithBadRequestPriceEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                null,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The price cannot be null"))
        );
    }

    @Test
    void postProductWithBadRequestPriceMin() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                -50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The price must be greater than 0"))
        );
    }

    @Test
    void postProductWithBadRequestImgEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The image cannot be blank"))
        );
    }

    @Test
    void postProductWithBadRequestStockEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                null,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The stock cannot be empty"))
        );
    }

    @Test
    void postProductWithBadRequestStockMin() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                -10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The stock must be greater than 0"))
        );
    }

    @Test
    void postProductWithBadRequestDescriptionEmpty() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                null,
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The description cannot be blank"))
        );
    }

    @Test
    void postProductWithBadRequestDescriptionLenght() throws Exception {
        var productDto = new ProductCreateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "D",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The description must be between 3 and 100 characters"))
        );
    }


    @Test
    void putProduct() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + idProduct1;
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        when(productService.update(anyString(), any(ProductUpdateDto.class))).thenReturn(productResponseDto1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ProductResponseDto productResponse = mapper.readValue(response.getContentAsString(), ProductResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(product1.getId(), productResponse.id()),
                () -> assertEquals(product1.getName(), productResponse.name())
        );
        verify(productService, times(1)).update(anyString(), any(ProductUpdateDto.class));
    }

    @Test
    void putProductIdNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + idProduct1;
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        when(productService.update(anyString(), any())).thenThrow(new ProductNotFound(idProduct1.toString()));
        assertAll(
                () -> assertEquals(404, mockMvc.perform(
                                put(LOCAL_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(productUpdateDto))
                                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getStatus())
        );
    }

    @Test
    void putProductWithBadRequest() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + idProduct1;
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "P",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        System.out.println(response.getContentAsString());

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("The name must be between 3 and 50 characters"))
        );
    }

    @Test
    void patchProduct() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );
        when(productService.update(anyString(), any(ProductUpdateDto.class))).thenReturn(productResponseDto1);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ProductResponseDto res = mapper.readValue(response.getContentAsString(), ProductResponseDto.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(product1.getId(), res.id()),
                () -> assertEquals(product1.getName(), res.name()),
                () -> assertEquals(product1.getWeight(), res.weight()),
                () -> assertEquals(product1.getPrice(), res.price()),
                () -> assertEquals(product1.getImg(), res.img()),
                () -> assertEquals(product1.getStock(), res.stock()),
                () -> assertEquals(product1.getDescription(), res.description()),
                () -> assertEquals(product1.getCategory(), res.category())
        );
        verify(productService, times(1)).update(anyString(), any(ProductUpdateDto.class));
    }

    @Test
    void patchProductNotFound() {
        var LOCAL_URL = BASE_URL + "/1";
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(
                "Product 1",
                2.5,
                50.0,
                "imagen1.jpg",
                10,
                "Descripcion del producto 1",
                category1
        );
        when(productService.update(anyString(), any(ProductUpdateDto.class))).thenThrow(new ProductNotFound("Producto no encontrado"));
        assertAll(
                () -> assertEquals(404, mockMvc.perform(
                                patch(LOCAL_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(productUpdateDto))
                                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getStatus())
        );
    }


    @Test
    void deleteProduct() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        doNothing().when(productService).deleteById(anyString());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
        verify(productService, times(1)).deleteById(anyString());
    }

    @Test
    void deleteProductNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        doThrow(new ProductNotFound("Producto no encontrado")).when(productService).deleteById(anyString());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(productService, times(1)).deleteById(anyString());
    }
}