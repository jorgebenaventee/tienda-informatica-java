package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.rest.categories.services.CategoryService;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.SupplierMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Supplier;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.SupplierRepository;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.SupplierServiceImpl;
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
class ProveedorServiceImplTest {


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

    private final Supplier supplier = Supplier.builder()
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

    private final SupplierResponseDto supplierResponseDto = new SupplierResponseDto(
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


    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);
    @Mock
    private SupplierRepository proveedorRepository;
    @Mock
    private SupplierMapper proveedorMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private SuppliersNotificationMapper proveedoresNotificationMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private SupplierServiceImpl proveedorService;


    @Test
    void findAll() {
        List<Supplier> expectedProveedor = Arrays.asList(supplier, supplier2);
        List<SupplierResponseDto> expectedProveedorResponseDto = Arrays.asList(supplierResponseDto, supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedProveedor);

        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Supplier.class))).thenReturn(supplierResponseDto);

        Page<SupplierResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        System.out.println(actualProveedorResponseDto.getTotalElements());

        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(2)).toProveedorDto(any(Supplier.class));

    }

    @Test
    void findAllByName() {
        Optional<String> name = Optional.of("Proveedor 1");
        List<Supplier> expectedProveedor = List.of(supplier);
        List<SupplierResponseDto> proveedorResponseDtos = List.of(supplierResponseDto);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Supplier.class))).thenReturn(supplierResponseDto);

        Page<SupplierResponseDto> actualProveedorResponseDto = proveedorService.findAll(name, Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertEquals(proveedorResponseDtos, actualProveedorResponseDto.getContent()),
                () -> assertEquals(1, actualProveedorResponseDto.getTotalPages()),
                () -> assertEquals(1, actualProveedorResponseDto.getTotalElements()),
                () -> assertEquals(0, actualProveedorResponseDto.getNumber()),
                () -> assertEquals(1, actualProveedorResponseDto.getSize())
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Supplier.class));
    }

    @Test
    void findAllByCategory() {
        Optional<String> categoryName = Optional.of("Categoria 2");
        List<Supplier> expectedProveedor = List.of(supplier2);
        List<SupplierResponseDto> expectedProveedorResponse = List.of(supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Supplier.class))).thenReturn(supplierResponseDto2);

        Page<SupplierResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), categoryName, Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertEquals(expectedProveedorResponse, actualProveedorResponseDto.getContent()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Supplier.class));
    }

    @Test
    void findAllByContact() {
        Optional<Integer> contactNumber = Optional.of(2);
        List<Supplier> expectedProveedor = List.of(supplier2);
        List<SupplierResponseDto> expectedProveedorResponse = List.of(supplierResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Supplier> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Supplier.class))).thenReturn(supplierResponseDto2);

        Page<SupplierResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), Optional.empty(), contactNumber, pageable);
        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertEquals(expectedProveedorResponse, actualProveedorResponseDto.getContent()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Supplier.class));
    }

    @Test
    void findByUUID() {
        UUID uuid = supplier.getId();
        when(proveedorRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(proveedorMapper.toProveedorDto(supplier)).thenReturn(supplierResponseDto);
        SupplierResponseDto actualProveedor = proveedorService.findByUUID(uuid.toString());
        assertAll(
                () -> assertEquals(supplier.getName(), actualProveedor.name()),
                () -> assertEquals(supplier.getContact(), actualProveedor.contact()),
                () -> assertEquals(supplier.getAddress(), actualProveedor.address()),
                () -> assertEquals(supplier.getDateOfHire(), actualProveedor.dateOfHire()),
                () -> assertEquals(supplier.getCategory(), actualProveedor.category())
        );
        verify(proveedorRepository, times(1)).findById(uuid);
        verify(proveedorMapper, times(1)).toProveedorDto(supplier);
    }

    @Test
    void findByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFound.class, () -> proveedorService.findByUUID(uuid.toString()));
        verify(proveedorRepository, times(1)).findById(uuid);
    }

    @Test
    void save() throws IOException {
        UUID uuid = UUID.randomUUID();
        SupplierCreateDto proveedorCreateDto = new SupplierCreateDto(
                "Proveedor Creado",
                1,
                "Calle 1",
                category1
        );

        Supplier expectedProveedor = Supplier.builder()
                .id(uuid)
                .name(proveedorCreateDto.name())
                .contact(proveedorCreateDto.contact())
                .address(proveedorCreateDto.address())
                .dateOfHire(LocalDateTime.now())
                .category(proveedorCreateDto.category())
                .build();

        SupplierResponseDto expectedProveedorResponseDto = new SupplierResponseDto(
                uuid,
                proveedorCreateDto.name(),
                proveedorCreateDto.contact(),
                proveedorCreateDto.address(),
                LocalDateTime.now(),
                proveedorCreateDto.category()
        );

        when(categoryService.findById(category1.getUuid())).thenReturn(category1);
        when(proveedorMapper.toProveedor(proveedorCreateDto)).thenReturn(expectedProveedor);
        when(proveedorRepository.save(expectedProveedor)).thenReturn(expectedProveedor);
        when(proveedorMapper.toProveedorDto(expectedProveedor)).thenReturn(expectedProveedorResponseDto);
        doNothing().when(webSocketHandler).sendMessage(any());

        SupplierResponseDto actualSupplier = proveedorService.save(proveedorCreateDto);

        assertAll(
                () -> assertEquals(uuid, actualSupplier.id()),
                () -> assertEquals(proveedorCreateDto.name(), actualSupplier.name()),
                () -> assertEquals(proveedorCreateDto.contact(), actualSupplier.contact()),
                () -> assertEquals(proveedorCreateDto.address(), actualSupplier.address()),
                () -> assertEquals(proveedorCreateDto.category(), actualSupplier.category())

        );


        verify(proveedorMapper, times(1)).toProveedor(proveedorCreateDto);
        verify(categoryService, times(1)).findById(category1.getUuid());
        verify(proveedorRepository, times(1)).save(expectedProveedor);
        verify(proveedorMapper, times(1)).toProveedorDto(expectedProveedor);
    }

    @Test
    void update() throws IOException {
        UUID uuid = supplier.getId();
        SupplierUpdateDto proveedorUpdateDto = new SupplierUpdateDto(
                "Proveedor Actualizado",
                2,
                "Calle 2",
                category2
        );
        Supplier proveedor = supplier;

        when(proveedorRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);
        when(proveedorMapper.toProveedor(proveedorUpdateDto, proveedor)).thenReturn(proveedor);
        when(proveedorMapper.toProveedorDto(proveedor)).thenReturn(supplierResponseDto);
        doNothing().when(webSocketHandler).sendMessage(any());

        SupplierResponseDto actualProveedor = proveedorService.update(proveedorUpdateDto, uuid.toString());

        assertAll(
                () -> assertEquals(proveedor.getName(), actualProveedor.name()),
                () -> assertEquals(proveedor.getContact(), actualProveedor.contact()),
                () -> assertEquals(proveedor.getAddress(), actualProveedor.address()),
                () -> assertEquals(proveedor.getDateOfHire(), actualProveedor.dateOfHire()),
                () -> assertEquals(proveedor.getCategory(), actualProveedor.category())
        );

        verify(proveedorRepository, times(1)).findById(uuid);
        verify(proveedorMapper, times(1)).toProveedor(proveedorUpdateDto, proveedor);
        verify(proveedorRepository, times(1)).save(proveedor);
        verify(proveedorMapper, times(1)).toProveedorDto(proveedor);
    }

    @Test
    void deleteByUUID() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        proveedorService.deleteByUUID(uuid.toString());
        verify(proveedorRepository, times(1)).deleteById(uuid);
    }

    @Test
    void deleteByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFound.class, () -> proveedorService.deleteByUUID(uuid.toString()));
        verify(proveedorRepository, times(1)).findById(uuid);
    }
}