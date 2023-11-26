package dev.clownsinformatics.tiendajava.rest.categories.controllers;

import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTestMock {
    private final Category category = Category.builder().name("DISNEY").build();
    private final Category category2 = Category.builder().name("MARVEL").build();

    @Mock
    private CategoryService categoryService;
    @Mock
    private PaginationLinksUtils paginationLinksUtils;
    @InjectMocks
    private CategoryRestController categoryRestController;

    @Test
    void getAll() {
        Optional<String> name = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/categories"));

        List<Category> expectedCategory = List.of(category, category2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(expectedCategory);

        when(categoryService.findAll(name, Optional.empty(), pageable)).thenReturn(expectedPage);

        PageResponse<Category> response = categoryRestController.getAll(name, Optional.empty(), 0, 10, "uuid", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(expectedPage),
                () -> assertEquals(response.content(), expectedPage.getContent())
        );
        verify(categoryService, times(1)).findAll(name, Optional.empty(), pageable);
    }

    @Test
    void getAllByName() {
        Optional<String> name = Optional.of("DISNEY");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/categories"));

        List<Category> expectedCategory = List.of(category);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(expectedCategory);

        when(categoryService.findAll(name, Optional.empty(), pageable)).thenReturn(expectedPage);

        PageResponse<Category> response = categoryRestController.getAll(name, Optional.empty(), 0, 10, "uuid", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(expectedPage),
                () -> assertEquals(response.content(), expectedPage.getContent())
        );
        verify(categoryService, times(1)).findAll(name, Optional.empty(), pageable);
    }

    @Test
    void getAllByIsDeleted(){
        Optional<Boolean> isDeleted = Optional.of(true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/categories"));

        List<Category> expectedCategory = List.of(category);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(expectedCategory);

        when(categoryService.findAll(Optional.empty(), isDeleted, pageable)).thenReturn(expectedPage);

        PageResponse<Category> response = categoryRestController.getAll(Optional.empty(), isDeleted, 0, 10, "uuid", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(expectedPage),
                () -> assertEquals(response.content(), expectedPage.getContent())
        );
        verify(categoryService, times(1)).findAll(Optional.empty(), isDeleted, pageable);
    }

    @Test
    void getAllByNameAndIsDeleted(){
        Optional<String> name = Optional.of("DISNEY");
        Optional<Boolean> isDeleted = Optional.of(true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/categories"));

        List<Category> expectedCategory = List.of(category);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(expectedCategory);

        when(categoryService.findAll(name, isDeleted, pageable)).thenReturn(expectedPage);

        PageResponse<Category> response = categoryRestController.getAll(name, isDeleted, 0, 10, "uuid", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(expectedPage),
                () -> assertEquals(response.content(), expectedPage.getContent())
        );
        verify(categoryService, times(1)).findAll(name, isDeleted, pageable);
    }

    @Test
    void getById() {
        when(categoryService.findById(category.getUuid())).thenReturn(category);
        ResponseEntity<Category> response = categoryRestController.getById(category.getUuid());
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(category, response.getBody())

        );
        verify(categoryService, times(1)).findById(category.getUuid());
    }

    @Test
    void save() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                category.getName(), category.isDeleted()
        );
        when(categoryService.save(categoryResponseDto)).thenReturn(category);
        ResponseEntity<Category> response = categoryRestController.save(categoryResponseDto);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(category, response.getBody())
        );
        verify(categoryService, times(1)).save(categoryResponseDto);
    }

    @Test
    void update() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                category.getName(), category.isDeleted()
        );
        when(categoryService.update(categoryResponseDto, category.getUuid())).thenReturn(category);
        ResponseEntity<Category> response = categoryRestController.update(categoryResponseDto, category.getUuid());
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(category, response.getBody())
        );
        verify(categoryService, times(1)).update(categoryResponseDto, category.getUuid());
    }

    @Test
    void delete() {
        doNothing().when(categoryService).delete(category.getUuid());
        ResponseEntity<Void> response = categoryRestController.delete(category.getUuid());
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(204, response.getStatusCodeValue())
        );
        verify(categoryService, times(1)).delete(category.getUuid());
    }
}