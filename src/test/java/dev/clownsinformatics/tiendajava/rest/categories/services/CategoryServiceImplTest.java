package dev.clownsinformatics.tiendajava.rest.categories.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.dto.CategoryResponseDto;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryConflict;
import dev.clownsinformatics.tiendajava.rest.categories.exceptions.CategoryNotFound;
import dev.clownsinformatics.tiendajava.rest.categories.mappers.CategoryMapper;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.CategoryNotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private final Category category = Category.builder().name("DISNEY").build();
    private final Category category2 = Category.builder().name("MARVEL").build();

    private final CategoryResponseDto categoryResponseDto1 = new CategoryResponseDto("DISNEY", false);

    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private CategoryNotificationMapper categoryNotificationMapper;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void findAll() {
        Optional<String> name = Optional.empty();
        Optional<Boolean> isDeleted = Optional.empty();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(List.of(category, category2));

        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        var res = categoryService.findAll(name,isDeleted, pageable);

        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty())
        );

        verify(categoryRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAllByName() {
        Optional<String> name = Optional.of("DISNEY");
        Optional<Boolean> isDeleted = Optional.empty();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        Page<Category> expectedPage = new PageImpl<>(List.of(category, category2));

        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        var res = categoryService.findAll(name,isDeleted, pageable);

        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty())
        );

        verify(categoryRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findById() {
        when(categoryRepository.findByUuid(category.getUuid())).thenReturn(Optional.of(category));
        Category actualCategory = categoryService.findById(category.getUuid());
        assertAll(
                () -> assertEquals(category, actualCategory),
                () -> assertEquals(category.getName(), actualCategory.getName())
        );
        verify(categoryRepository, times(1)).findByUuid(category.getUuid());
    }

    @Test
    void findByIdNotFound() {
        when(categoryRepository.findByUuid(category.getUuid())).thenReturn(Optional.empty());
        var res = assertThrows(CategoryNotFound.class, () -> categoryService.findById(category.getUuid()));
        assertEquals("Category not found", res.getMessage());
        verify(categoryRepository, times(1)).findByUuid(category.getUuid());
    }

    @Test
    void save() throws IOException {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("DISNEY2", false);
        Category expectedCategory = Category.builder().name("DISNEY2").build();

        when(categoryRepository.findByName("DISNEY2")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        Category actualCategory = categoryService.save(categoryResponseDto);

        assertEquals(expectedCategory, actualCategory);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void saveAlreadyExist() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("DISNEY", false);

        when(categoryRepository.findByName("DISNEY")).thenReturn(Optional.of(category));

        var res = assertThrows(CategoryConflict.class, () -> categoryService.save(categoryResponseDto));
        assertEquals("Category already exists", res.getMessage());

        verify(categoryRepository, times(1)).findByName("DISNEY");
    }

    @Test
    void update() {
        Category expectedCategory = Category.builder().name("DISNEY3").build();
        Category categoryToUpdate = Category.builder().name("DISNEY3").build();
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto("DISNEY3", false);

        when(categoryRepository.findByUuid(categoryToUpdate.getUuid())).thenReturn(Optional.of(categoryToUpdate));
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);

        Category actualCategory = categoryService.update(categoryResponseDto, categoryToUpdate.getUuid());

        assertEquals(expectedCategory, actualCategory);

        verify(categoryRepository, times(1)).findByUuid(categoryToUpdate.getUuid());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteById() {
        when(categoryRepository.findByUuid(category.getUuid())).thenReturn(Optional.of(category));
        categoryService.delete(category.getUuid());
        verify(categoryRepository, times(1)).findByUuid(category.getUuid());
        verify(categoryRepository, times(1)).deleteById(category.getUuid());
    }
}