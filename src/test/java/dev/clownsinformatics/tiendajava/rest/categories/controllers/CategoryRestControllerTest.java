package dev.clownsinformatics.tiendajava.rest.categories.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryNotFound;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {
    private final String BASE_URL = "/api/categories";
    private final UUID uuid = UUID.fromString("d69cf3db-b77d-4181-b3cd-5ca8107fb6a8");
    private final UUID uuid2 = UUID.fromString("d69cf3db-b77d-4181-b3cd-5ca8107fb6a9");
    private final Category categoria = Category.builder().uuid(uuid).name("Category 1").build();
    private final Category categoria2 = Category.builder().uuid(uuid2).name("Category 2").build();
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private JacksonTester<Category> json;

    @Autowired
    public CategoryRestControllerTest(CategoryService categoryService) {
        this.categoryService = categoryService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAll() throws Exception {
        var categoryList = List.of(categoria, categoria2);
        Page<Category> page = new PageImpl<>(categoryList);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());

        when(categoryService.findAll(Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Category> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size()),
                () -> assertEquals("Category 1", res.content().get(0).getName()),
                () -> assertEquals("Category 2", res.content().get(1).getName())

        );
        verify(categoryService, times(1)).findAll(Optional.empty(), pageable);
    }

    @Test
    void getAllByName() throws Exception {
        var LOCAL_URL = BASE_URL + "?name=Category 1";
        var categoryList = List.of(categoria);
        Optional<String> name = Optional.of("Category 1");
        Page<Category> page = new PageImpl<>(categoryList);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());

        when(categoryService.findAll(name, pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Category> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals("Category 1", res.content().get(0).getName())

        );
        verify(categoryService, times(1)).findAll(name, pageable);
    }

    @Test
    void getById() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + categoria.getUuid().toString();
        when(categoryService.findById(categoria.getUuid())).thenReturn(categoria);

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Category actualCategory = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Category 1", actualCategory.getName()),
                () -> assertEquals(categoria.getName(), actualCategory.getName())
        );
        verify(categoryService, times(1)).findById(categoria.getUuid());
    }

    @Test
    void getByIdNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/d69cf3db-b77d-4181-b3cd-5ca8107fb6a8";
        when(categoryService.findById(any(UUID.class))).thenThrow(new CategoryNotFound("Category not found"));

        MockHttpServletResponse response = mockMvc.perform(
                        get(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        verify(categoryService, times(1)).findById(any(UUID.class));
    }

    @Test
    void save() throws Exception {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("Category 1");
        when(categoryService.save(categoryResponseDto)).thenReturn(categoria);

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoryResponseDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Category actualCategory = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals("Category 1", actualCategory.getName()),
                () -> assertEquals(categoria.getName(), actualCategory.getName())
        );
        verify(categoryService, times(1)).save(categoryResponseDto);
    }

    @Test
    void saveBadRequest() throws Exception {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("");
        when(categoryService.save(categoryResponseDto)).thenReturn(categoria);

        MockHttpServletResponse response = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoryResponseDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(categoryService, times(0)).save(categoryResponseDto);
    }

    @Test
    void update() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + categoria.getUuid().toString();
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("Category 1");
        when(categoryService.update(categoryResponseDto, categoria.getUuid())).thenReturn(categoria);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoryResponseDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Category actualCategory = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Category 1", actualCategory.getName()),
                () -> assertEquals(categoria.getName(), actualCategory.getName())
        );
        verify(categoryService, times(1)).update(categoryResponseDto, categoria.getUuid());
    }

    @Test
    void putBadRequest() throws Exception {
        var LOCAL_URL = BASE_URL + "/1";
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("");
        when(categoryService.update(categoryResponseDto, categoria.getUuid())).thenReturn(categoria);

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoryResponseDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(categoryService, times(0)).update(categoryResponseDto, categoria.getUuid());
    }

    @Test
    void putNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + categoria.getUuid().toString();
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("Category 1");
        when(categoryService.update(categoryResponseDto, categoria.getUuid())).thenThrow(new CategoryNotFound("Category not found"));

        MockHttpServletResponse response = mockMvc.perform(
                        put(LOCAL_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoryResponseDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(categoryService, times(1)).update(categoryResponseDto, categoria.getUuid());
    }

    @Test
    void deleteCategory() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + categoria.getUuid().toString();
        doNothing().when(categoryService).delete(categoria.getUuid());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
        verify(categoryService, times(1)).delete(categoria.getUuid());
    }

    @Test
    void deleteCategoryNotFound() throws Exception {
        var LOCAL_URL = BASE_URL + "/" + categoria.getUuid().toString();
        doThrow(new CategoryNotFound("Category not found")).when(categoryService).delete(categoria.getUuid());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(LOCAL_URL)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(categoryService, times(1)).delete(categoria.getUuid());
    }
}