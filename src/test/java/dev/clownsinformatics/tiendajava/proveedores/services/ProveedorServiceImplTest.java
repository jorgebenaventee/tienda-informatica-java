package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.categories.repositories.CategoryRepository;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.ProveedorRepository;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.ProveedorServiceImpl;
import dev.clownsinformatics.tiendajava.websockets.notifications.mapper.ProveedoresNotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

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

    private final Proveedor proveedor1 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .name("Proveedor 1")
            .contact(1)
            .address("Calle 1")
            .dateOfHire(LocalDateTime.now())
            .category(category1)
            .build();


    private final Proveedor proveedor2 = Proveedor.builder()
            .idProveedor(UUID.randomUUID())
            .name("Proveedor 2")
            .contact(2)
            .address("Calle 2")
            .dateOfHire(LocalDateTime.now())
            .category(category2)
            .build();

    private final ProveedorResponseDto proveedorResponseDto1 = new ProveedorResponseDto(
            UUID.randomUUID(),
            "Proveedor 1",
            1,
            "Calle 1",
            LocalDateTime.now(),
            category1
    );

    private final ProveedorResponseDto proveedorResponseDto2 = new ProveedorResponseDto(
            UUID.randomUUID(),
            "Proveedor 2",
            2,
            "Calle 2",
            LocalDateTime.now(),
            category2
    );


    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);
    @Mock
    private ProveedorRepository proveedorRepository;
    @Mock
    private ProveedorMapper proveedorMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private ProveedoresNotificationMapper proveedoresNotificationMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProveedorServiceImpl proveedorService;


    @Test
    void findAll() {
        List<Proveedor> expectedProveedor = Arrays.asList(proveedor1, proveedor2);
        List<ProveedorResponseDto> expectedProveedorResponseDto = Arrays.asList(proveedorResponseDto1, proveedorResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Proveedor> expectedPage = new PageImpl<>(expectedProveedor);

        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Proveedor.class))).thenReturn(proveedorResponseDto1);

        Page<ProveedorResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        System.out.println(actualProveedorResponseDto.getTotalElements());

        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(2)).toProveedorDto(any(Proveedor.class));

    }

    @Test
    void findAllByName() {
        Optional<String> name = Optional.of("Proveedor 1");
        List<Proveedor> expectedProveedor = List.of(proveedor1);
        List<ProveedorResponseDto> proveedorResponseDtos = List.of(proveedorResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Proveedor> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Proveedor.class))).thenReturn(proveedorResponseDto1);

        Page<ProveedorResponseDto> actualProveedorResponseDto = proveedorService.findAll(name, Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertEquals(proveedorResponseDtos, actualProveedorResponseDto.getContent()),
                () -> assertEquals(1, actualProveedorResponseDto.getTotalPages()),
                () -> assertEquals(1, actualProveedorResponseDto.getTotalElements()),
                () -> assertEquals(0, actualProveedorResponseDto.getNumber()),
                () -> assertEquals(1, actualProveedorResponseDto.getSize())
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Proveedor.class));
    }

    @Test
    void findAllByCategory() {
        Optional<String> categoryName = Optional.of("Categoria 2");
        List<Proveedor> expectedProveedor = List.of(proveedor2);
        List<ProveedorResponseDto> expectedProveedorResponse = List.of(proveedorResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Proveedor> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Proveedor.class))).thenReturn(proveedorResponseDto2);

        Page<ProveedorResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), categoryName, Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertEquals(expectedProveedorResponse, actualProveedorResponseDto.getContent()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Proveedor.class));
    }

    @Test
    void findAllByContact() {
        Optional<Integer> contactNumber = Optional.of(2);
        List<Proveedor> expectedProveedor = List.of(proveedor2);
        List<ProveedorResponseDto> expectedProveedorResponse = List.of(proveedorResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Proveedor> expectedPage = new PageImpl<>(expectedProveedor);
        when(proveedorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(proveedorMapper.toProveedorDto(any(Proveedor.class))).thenReturn(proveedorResponseDto2);

        Page<ProveedorResponseDto> actualProveedorResponseDto = proveedorService.findAll(Optional.empty(), Optional.empty(), contactNumber, pageable);
        assertAll(
                () -> assertNotNull(actualProveedorResponseDto),
                () -> assertEquals(expectedProveedorResponse, actualProveedorResponseDto.getContent()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0),
                () -> assertFalse(actualProveedorResponseDto.isEmpty()),
                () -> assertTrue(actualProveedorResponseDto.getTotalElements() > 0)
        );

        verify(proveedorRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(proveedorMapper, times(1)).toProveedorDto(any(Proveedor.class));
    }

    @Test
    void findByUUID() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor1));
        Proveedor actualProveedor = proveedorService.findByUUID(uuid.toString());
        assertAll(
                () -> assertEquals(proveedor1.getIdProveedor(), actualProveedor.getIdProveedor()),
                () -> assertEquals(proveedor1.getName(), actualProveedor.getName()),
                () -> assertEquals(proveedor1.getContact(), actualProveedor.getContact()),
                () -> assertEquals(proveedor1.getAddress(), actualProveedor.getAddress()),
                () -> assertEquals(proveedor1.getDateOfHire(), actualProveedor.getDateOfHire())
        );
        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
    }

    @Test
    void findByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.empty());
        assertThrows(ProveedorNotFound.class, () -> proveedorService.findByUUID(uuid.toString()));
        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
    }

    @Test
    void save() {
        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto(
                "Proveedor Creado",
                1,
                "Calle 1",
                category1
        );

        Proveedor expectedProveedor = Proveedor.builder()
                .idProveedor(UUID.randomUUID())
                .name(proveedorCreateDto.name())
                .contact(proveedorCreateDto.contact())
                .address(proveedorCreateDto.address())
                .dateOfHire(LocalDateTime.now())
                .category(proveedorCreateDto.category())
                .build();

        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(expectedProveedor);
        when(proveedorMapper.toProveedor(proveedorCreateDto, expectedProveedor.getIdProveedor())).thenReturn(expectedProveedor);

        Proveedor actualProveedor = proveedorService.save(proveedorCreateDto);

        assertAll(
                () -> assertEquals(expectedProveedor, actualProveedor),
                () -> assertEquals(expectedProveedor.getIdProveedor(), actualProveedor.getIdProveedor()),
                () -> assertEquals(expectedProveedor.getName(), actualProveedor.getName()),
                () -> assertEquals(expectedProveedor.getContact(), actualProveedor.getContact()),
                () -> assertEquals(expectedProveedor.getAddress(), actualProveedor.getAddress()),
                () -> assertEquals(expectedProveedor.getDateOfHire(), actualProveedor.getDateOfHire())
        );

        verify(proveedorMapper, times(1)).toProveedor(proveedorCreateDto, expectedProveedor.getIdProveedor());
    }

    @Test
    void update() {
        UUID uuid = UUID.randomUUID();
        ProveedorUpdateDto proveedorUpdateDto = new ProveedorUpdateDto(
                "Proveedor Actualizado",
                2,
                "Calle 2",
                category2
        );
        Proveedor proveedor = proveedor1;

        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor));
        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);
        when(proveedorMapper.toProveedor(proveedorUpdateDto, proveedor)).thenReturn(proveedor);

        Proveedor actualProveedor = proveedorService.update(proveedorUpdateDto, uuid.toString());

        assertAll(
                () -> assertEquals(proveedor.getIdProveedor(), actualProveedor.getIdProveedor()),
                () -> assertEquals(proveedor.getName(), actualProveedor.getName()),
                () -> assertEquals(proveedor.getContact(), actualProveedor.getContact()),
                () -> assertEquals(proveedor.getAddress(), actualProveedor.getAddress()),
                () -> assertEquals(proveedor.getDateOfHire(), actualProveedor.getDateOfHire())
        );

        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
        verify(proveedorMapper, times(1)).toProveedor(proveedorUpdateDto, proveedor);
    }

    @Test
    void deleteByUUID() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor1));
        proveedorService.deleteByUUID(uuid.toString());
        verify(proveedorRepository, times(1)).deleteByIdProveedor(uuid);
    }

    @Test
    void deleteByUUIDNotFound() {
        UUID uuid = UUID.randomUUID();
        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.empty());
        assertThrows(ProveedorNotFound.class, () -> proveedorService.deleteByUUID(uuid.toString()));
        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
    }
}