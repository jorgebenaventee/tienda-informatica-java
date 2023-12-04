package dev.clownsinformatics.tiendajava.rest.suppliers.services;

import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.suppliers.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.suppliers.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.suppliers.repositories.SupplierRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.SuppliersNotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {
    LocalDateTime now = LocalDateTime.now();

    private final Category category1 = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 1")
            .createdAt(now)
            .updatedAt(now)
            .build();

    private final Category category2 = Category.builder()
            .uuid(UUID.randomUUID())
            .name("Category 2")
            .createdAt(now)
            .updatedAt(now)
            .build();

    private final Supplier supplier = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(now)
            .category(category1)
            .isDeleted(false)
            .build();


    private final Supplier supplier2 = Supplier.builder()
            .id(UUID.randomUUID())
            .name("Supplier 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(now)
            .category(category2)
            .isDeleted(false)
            .build();

    private final SupplierResponseDto supplierResponseDto = new SupplierResponseDto(
            UUID.randomUUID(),
            "Supplier 1",
            1,
            "Calle 1",
            now,
            category1,
            false
    );

    private final SupplierResponseDto supplierResponseDto2 = new SupplierResponseDto(
            UUID.randomUUID(),
            "Supplier 2",
            2,
            "Calle 2",
            now,
            category2,
            false
    );


    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private SupplierMapper supplierMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private SuppliersNotificationMapper suppliersNotificationMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private SupplierServiceImpl supplierService;


    @Test
    void findAll() {
        List<Supplier> expectedSupplier = Arrays.asList(supplier, supplier2);
        List<SupplierResponseDto> expectedSupplierResponseDto = Arrays.asList(supplierResponseDto, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedSupplier);

        when(supplierRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(supplierMapper.toSupplierDto(any(Supplier.class))).thenReturn(supplierResponseDto);

        Page<SupplierResponseDto> actualSupplierResponseDto = supplierService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        System.out.println(actualSupplierResponseDto.getTotalElements());

        assertAll(
                () -> assertNotNull(actualSupplierResponseDto),
                () -> assertFalse(actualSupplierResponseDto.isEmpty()),
                () -> assertTrue(actualSupplierResponseDto.getTotalElements() > 0)
        );

        verify(supplierRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(supplierMapper, times(2)).toSupplierDto(any(Supplier.class));

    }

    @Test
    void findAllByName() {
        Optional<String> name = Optional.of("Supplier 1");
        List<Supplier> expectedSupplier = List.of(supplier);
        List<SupplierResponseDto> supplierResponseDtos = List.of(supplierResponseDto);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedSupplier);
        when(supplierRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(supplierMapper.toSupplierDto(any(Supplier.class))).thenReturn(supplierResponseDto);

        Page<SupplierResponseDto> actualSupplierResponseDto = supplierService.findAll(name, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertEquals(supplierResponseDtos, actualSupplierResponseDto.getContent()),
                () -> assertEquals(1, actualSupplierResponseDto.getTotalPages()),
                () -> assertEquals(1, actualSupplierResponseDto.getTotalElements()),
                () -> assertEquals(0, actualSupplierResponseDto.getNumber()),
                () -> assertEquals(1, actualSupplierResponseDto.getSize())
        );

        verify(supplierRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(supplierMapper, times(1)).toSupplierDto(any(Supplier.class));
    }

    @Test
    void findAllByCategory() {
        Optional<String> categoryName = Optional.of("Categoria 2");
        List<Supplier> expectedSupplier = List.of(supplier2);
        List<SupplierResponseDto> expectedSupplierResponse = List.of(supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedSupplier);
        when(supplierRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(supplierMapper.toSupplierDto(any(Supplier.class))).thenReturn(supplierResponseDto2);

        Page<SupplierResponseDto> actualSupplierResponseDto = supplierService.findAll(Optional.empty(), categoryName, Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(actualSupplierResponseDto),
                () -> assertEquals(expectedSupplierResponse, actualSupplierResponseDto.getContent()),
                () -> assertTrue(actualSupplierResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualSupplierResponseDto.isEmpty()),
                () -> assertTrue(actualSupplierResponseDto.getTotalElements() > 0)
        );

        verify(supplierRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(supplierMapper, times(1)).toSupplierDto(any(Supplier.class));
    }

    @Test
    void findAllByContact() {
        Optional<Integer> contactNumber = Optional.of(2);
        List<Supplier> expectedSupplier = List.of(supplier2);
        List<SupplierResponseDto> expectedSupplierResponse = List.of(supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedSupplier);
        when(supplierRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(supplierMapper.toSupplierDto(any(Supplier.class))).thenReturn(supplierResponseDto2);

        Page<SupplierResponseDto> actualSupplierResponseDto = supplierService.findAll(Optional.empty(), Optional.empty(), contactNumber, Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(actualSupplierResponseDto),
                () -> assertEquals(expectedSupplierResponse, actualSupplierResponseDto.getContent()),
                () -> assertTrue(actualSupplierResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualSupplierResponseDto.isEmpty()),
                () -> assertTrue(actualSupplierResponseDto.getTotalElements() > 0)
        );

        verify(supplierRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(supplierMapper, times(1)).toSupplierDto(any(Supplier.class));
    }

    @Test
    void findByUUID() throws IOException {
        UUID uuid = supplier.getId();
        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toSupplierDto(supplier)).thenReturn(supplierResponseDto);
        doNothing().when(webSocketHandler).sendMessage(any());
        SupplierResponseDto actualSupplier = supplierService.findByUUID(uuid.toString());
        assertAll(
                () -> assertEquals(supplier.getName(), actualSupplier.name()),
                () -> assertEquals(supplier.getContact(), actualSupplier.contact()),
                () -> assertEquals(supplier.getAddress(), actualSupplier.address()),
                () -> assertEquals(supplier.getDateOfHire(), actualSupplier.dateOfHire()),
                () -> assertEquals(supplier.getCategory(), actualSupplier.category())
        );
        verify(supplierRepository, times(1)).findById(uuid);
        verify(supplierMapper, times(1)).toSupplierDto(supplier);
    }

    @Test
    void findByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(supplierRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFound.class, () -> supplierService.findByUUID(uuid.toString()));
        verify(supplierRepository, times(1)).findById(uuid);
    }

    @Test
    void save() throws IOException {
        UUID uuid = UUID.randomUUID();
        SupplierCreateDto supplierCreateDto = new SupplierCreateDto(
                "Supplier Creado",
                1,
                "Calle 1",
                category1,
                false
        );

        Supplier expectedSupplier = Supplier.builder()
                .id(uuid)
                .name(supplierCreateDto.name())
                .contact(supplierCreateDto.contact())
                .address(supplierCreateDto.address())
                .dateOfHire(now)
                .category(supplierCreateDto.category())
                .build();

        SupplierResponseDto expectedSupplierResponseDto = new SupplierResponseDto(
                uuid,
                supplierCreateDto.name(),
                supplierCreateDto.contact(),
                supplierCreateDto.address(),
                now,
                supplierCreateDto.category(),
                supplierCreateDto.isDeleted()
        );

        when(categoryService.findById(category1.getUuid())).thenReturn(category1);
        when(supplierMapper.toSupplier(supplierCreateDto)).thenReturn(expectedSupplier);
        when(supplierRepository.save(expectedSupplier)).thenReturn(expectedSupplier);
        when(supplierMapper.toSupplierDto(expectedSupplier)).thenReturn(expectedSupplierResponseDto);
        doNothing().when(webSocketHandler).sendMessage(any());

        SupplierResponseDto actualSupplier = supplierService.save(supplierCreateDto);

        assertAll(
                () -> assertEquals(uuid, actualSupplier.id()),
                () -> assertEquals(supplierCreateDto.name(), actualSupplier.name()),
                () -> assertEquals(supplierCreateDto.contact(), actualSupplier.contact()),
                () -> assertEquals(supplierCreateDto.address(), actualSupplier.address()),
                () -> assertEquals(supplierCreateDto.category(), actualSupplier.category())

        );


        verify(supplierMapper, times(1)).toSupplier(supplierCreateDto);
        verify(categoryService, times(1)).findById(category1.getUuid());
        verify(supplierRepository, times(1)).save(expectedSupplier);
        verify(supplierMapper, times(1)).toSupplierDto(expectedSupplier);
    }

    @Test
    void update() throws IOException {
        UUID uuid = supplier.getId();
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto(
                "Supplier Actualizado",
                2,
                "Calle 2",
                category2,
                false
        );
        Supplier supplier1 = supplier;

        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier1));
        when(supplierRepository.save(supplier1)).thenReturn(supplier1);
        when(supplierMapper.toSupplier(supplierUpdateDto, supplier1)).thenReturn(supplier1);
        when(supplierMapper.toSupplierDto(supplier1)).thenReturn(supplierResponseDto);
        doNothing().when(webSocketHandler).sendMessage(any());

        SupplierResponseDto actualSupplier = supplierService.update(supplierUpdateDto, uuid.toString());

        assertAll(
                () -> assertEquals(supplier1.getName(), actualSupplier.name()),
                () -> assertEquals(supplier1.getContact(), actualSupplier.contact()),
                () -> assertEquals(supplier1.getAddress(), actualSupplier.address()),
                () -> assertEquals(supplier1.getDateOfHire(), actualSupplier.dateOfHire()),
                () -> assertEquals(supplier1.getCategory(), actualSupplier.category())
        );

        verify(supplierRepository, times(1)).findById(uuid);
        verify(supplierMapper, times(1)).toSupplier(supplierUpdateDto, supplier1);
        verify(supplierRepository, times(1)).save(supplier1);
        verify(supplierMapper, times(1)).toSupplierDto(supplier1);
    }

    @Test
    void deleteByUUID() {
        UUID uuid = UUID.randomUUID();
        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        supplierService.deleteByUUID(uuid.toString());
        verify(supplierRepository, times(1)).deleteById(uuid);
    }

    @Test
    void deleteByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(supplierRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFound.class, () -> supplierService.deleteByUUID(uuid.toString()));
        verify(supplierRepository, times(1)).findById(uuid);
    }
}