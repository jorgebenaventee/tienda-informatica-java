package dev.clownsinformatics.tiendajava.rest.proveedores.controllers;

import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.controllers.SupplierController;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.SupplierService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierControllerTestMock {


    private final Category category1 = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Category category2 = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 2")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Supplier supplier1 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category1)
            .build();


    private final Supplier supplier2 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Proveedor 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category2)
            .build();

    private final SupplierResponseDto supplierResponseDto1 = new SupplierResponseDto(
            UUID.randomUUID(),
            "Proveedor 1",
            1,
            "Calle 1",
            LocalDateTime.now(),
            category1
    );

    private final SupplierResponseDto supplierResponseDto2 = new SupplierResponseDto(
            UUID.randomUUID(),
            "Proveedor 2",
            2,
            "Calle 2",
            LocalDateTime.now(),
            category2
    );

    @Mock
    private SupplierService supplierService;
    @Mock
    private PaginationLinksUtils paginationLinksUtils;
    @InjectMocks
    private SupplierController supplierController;

    @Test
    void getAll() {
        Optional<String> category = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<Integer> contact = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/suppliers"));
        List<SupplierResponseDto> supplierResponseDtoList = List.of(supplierResponseDto1, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<SupplierResponseDto> expectedPage = new PageImpl<>(supplierResponseDtoList);
        when(supplierService.findAll(category, name, contact, pageable)).thenReturn(expectedPage);

        PageResponse<SupplierResponseDto> pageResponse = supplierController.getAll(category, name, contact, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(pageResponse),
                () -> assertEquals(expectedPage.getContent(), pageResponse.content())
        );

        verify(supplierService, times(1)).findAll(category, name, contact, pageable);

    }

    @Test
    void getAllByCategory() {
        Optional<String> category = Optional.of("Category 1");
        Optional<String> name = Optional.empty();
        Optional<Integer> contact = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/suppliers"));
        List<SupplierResponseDto> supplierResponseDtoList = List.of(supplierResponseDto1, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<SupplierResponseDto> expectedPage = new PageImpl<>(supplierResponseDtoList);
        when(supplierService.findAll(category, name, contact, pageable)).thenReturn(expectedPage);

        PageResponse<SupplierResponseDto> pageResponse = supplierController.getAll(category, name, contact, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(pageResponse),
                () -> assertEquals(expectedPage.getContent(), pageResponse.content())
        );

        verify(supplierService, times(1)).findAll(category, name, contact, pageable);

    }

    @Test
    void getAllByName() {
        Optional<String> category = Optional.empty();
        Optional<String> name = Optional.of("Proveedor 1");
        Optional<Integer> contact = Optional.empty();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/suppliers"));
        List<SupplierResponseDto> supplierResponseDtoList = List.of(supplierResponseDto1, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<SupplierResponseDto> expectedPage = new PageImpl<>(supplierResponseDtoList);
        when(supplierService.findAll(category, name, contact, pageable)).thenReturn(expectedPage);

        PageResponse<SupplierResponseDto> pageResponse = supplierController.getAll(category, name, contact, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(pageResponse),
                () -> assertEquals(expectedPage.getContent(), pageResponse.content())
        );

        verify(supplierService, times(1)).findAll(category, name, contact, pageable);

    }

    @Test
    void getAllByContact() {
        Optional<String> category = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<Integer> contact = Optional.of(1);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/suppliers"));
        List<SupplierResponseDto> supplierResponseDtoList = List.of(supplierResponseDto1, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<SupplierResponseDto> expectedPage = new PageImpl<>(supplierResponseDtoList);
        when(supplierService.findAll(category, name, contact, pageable)).thenReturn(expectedPage);

        PageResponse<SupplierResponseDto> pageResponse = supplierController.getAll(category, name, contact, 0, 10, "id", "asc", request).getBody();

        assertAll(
                () -> assertNotNull(pageResponse),
                () -> assertEquals(expectedPage.getContent(), pageResponse.content())
        );

        verify(supplierService, times(1)).findAll(category, name, contact, pageable);

    }

    @Test
    void getSupplierByUUID() {
        UUID uuid = supplier1.getId();
        when(supplierService.findByUUID(uuid.toString())).thenReturn(supplierResponseDto1);
        SupplierResponseDto supplierResponseDto = supplierController.getSupplierByUUID(uuid.toString());
        assertAll(
                () -> assertNotNull(supplierResponseDto),
                () -> assertEquals(supplierResponseDto1, supplierResponseDto)
        );
        verify(supplierService, times(1)).findByUUID(uuid.toString());
    }

    @Test
    void createSupplier() {
        SupplierCreateDto supplierCreateDto = new SupplierCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.save(supplierCreateDto)).thenReturn(supplierResponseDto1);
        ResponseEntity<SupplierResponseDto> responseEntity = supplierController.createSupplier(supplierCreateDto);
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(201, responseEntity.getStatusCode().value()),
                () -> assertEquals(supplierResponseDto1, responseEntity.getBody())
        );
        verify(supplierService, times(1)).save(supplierCreateDto);
    }

    @Test
    void updateSupplier() {
        UUID uuid = supplier2.getId();
        SupplierUpdateDto proveedorUpdateDto = new SupplierUpdateDto(
                "Proveedor 2",
                2,
                "Calle 2",
                category2
        );
        when(supplierService.update(proveedorUpdateDto, uuid.toString())).thenReturn(supplierResponseDto2);
        ResponseEntity<SupplierResponseDto> responseEntity = supplierController.updateSupplier(uuid.toString(), proveedorUpdateDto);
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(200, responseEntity.getStatusCode().value()),
                () -> assertEquals(supplierResponseDto2, responseEntity.getBody())
        );
        verify(supplierService, times(1)).update(proveedorUpdateDto, uuid.toString());
    }

    @Test
    void updateSupplierPatch() {
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto(
                "Proveedor 2",
                2,
                "Calle 2",
                category2
        );
        when(supplierService.update(supplierUpdateDto, supplier1.getId().toString())).thenReturn(supplierResponseDto2);
        ResponseEntity<SupplierResponseDto> response = supplierController.updateSupplierPatch(supplier1.getId().toString(), supplierUpdateDto);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(supplierUpdateDto.name(), response.getBody().name()),
                () -> assertEquals(supplierUpdateDto.contact(), response.getBody().contact()),
                () -> assertEquals(supplierUpdateDto.address(), response.getBody().address()),
                () -> assertEquals(supplierUpdateDto.category(), response.getBody().category())
        );
        verify(supplierService, times(1)).update(supplierUpdateDto, supplier1.getId().toString());
    }


    @Test
    void deleteSupplier() {
        ResponseEntity<Void> responseEntity = supplierController.deleteSupplier(supplier1.getId().toString());
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(204, responseEntity.getStatusCode().value())
        );
        verify(supplierService, times(1)).deleteByUUID(supplier1.getId().toString());
    }
}