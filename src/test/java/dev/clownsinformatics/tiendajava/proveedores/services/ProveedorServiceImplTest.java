//package dev.clownsinformatics.tiendajava.proveedores.services;
//
//import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
//import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
//import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
//import dev.clownsinformatics.tiendajava.rest.proveedores.mapper.ProveedorMapper;
//import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
//import dev.clownsinformatics.tiendajava.rest.proveedores.repositories.ProveedorRepository;
//import dev.clownsinformatics.tiendajava.rest.proveedores.services.ProveedorServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ProveedorServiceImplTest {
//
//    Proveedor proveedor1 = Proveedor.builder()
//            .idProveedor(UUID.randomUUID())
//            .nombre("Proveedor 1")
//            .contacto(1)
//            .direccion("Calle 1")
//            .fechaContratacion(LocalDate.now())
//            .build();
//
//    Proveedor proveedor2 = Proveedor.builder()
//            .idProveedor(UUID.randomUUID())
//            .nombre("Proveedor 2")
//            .contacto(2)
//            .direccion("Calle 2")
//            .fechaContratacion(LocalDate.now())
//            .build();
//
//    @Mock
//    private ProveedorRepository proveedorRepository;
//    @Mock
//    private ProveedorMapper proveedorMapper;
//    @InjectMocks
//    private ProveedorServiceImpl proveedorService;
//    @Captor
//    private ArgumentCaptor<Proveedor> proveedorArgumentCaptor;
//
//
//    @Test
//    void findAll() {
//        List<Proveedor> proveedorEsperado = Arrays.asList(proveedor1, proveedor2);
//        when(proveedorRepository.findAll()).thenReturn(proveedorEsperado);
//        List<Proveedor> proveedorActual = proveedorService.findAll(null, null);
//        assertIterableEquals(proveedorEsperado, proveedorActual);
//        verify(proveedorRepository, times(1)).findAll();
//    }
//
//    @Test
//    void findByUUID() {
//        UUID uuid = UUID.randomUUID();
//        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor1));
//        Proveedor proveedorActual = proveedorService.findByUUID(uuid.toString());
//        assertAll(
//                () -> assertEquals(proveedor1.getIdProveedor(), proveedorActual.getIdProveedor()),
//                () -> assertEquals(proveedor1.getName(), proveedorActual.getName()),
//                () -> assertEquals(proveedor1.getContacto(), proveedorActual.getContacto()),
//                () -> assertEquals(proveedor1.getDireccion(), proveedorActual.getDireccion()),
//                () -> assertEquals(proveedor1.getFechaContratacion(), proveedorActual.getFechaContratacion())
//        );
//        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
//    }
//
//    @Test
//    void findByUUIDNotFound() {
//        UUID uuid = UUID.randomUUID();
//        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.empty());
//        assertThrows(ProveedorNotFound.class, () -> proveedorService.findByUUID(uuid.toString()));
//        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
//    }
//
//    @Test
//    void findByNombre() {
//        String nombre = "Proveedor 1";
//        List<Proveedor> proveedorEsperado = Arrays.asList(proveedor1);
//        when(proveedorRepository.getByNameContainingIgnoreCase(nombre)).thenReturn(proveedorEsperado);
//        List<Proveedor> proveedorActual = proveedorService.findAll(nombre, null);
//        assertIterableEquals(proveedorEsperado, proveedorActual);
//        verify(proveedorRepository, times(1)).getByNameContainingIgnoreCase(nombre);
//
//    }
//
//    @Test
//    void findByDireccion() {
//        String direccion = "Calle 1";
//        List<Proveedor> proveedorEsperado = Arrays.asList(proveedor1);
//        when(proveedorRepository.getByAddressContainingIgnoreCase(direccion)).thenReturn(proveedorEsperado);
//        List<Proveedor> proveedorActual = proveedorService.findAll(null, direccion);
//        assertIterableEquals(proveedorEsperado, proveedorActual);
//        verify(proveedorRepository, times(1)).getByAddressContainingIgnoreCase(direccion);
//
//    }
//
//    @Test
//    void findByNombreAndDireccion() {
//        String nombre = "Proveedor 1";
//        String direccion = "Calle 1";
//        List<Proveedor> proveedorEsperado = Arrays.asList(proveedor1);
//        when(proveedorRepository.getByNameAndAddressContainingIgnoreCase(nombre, direccion)).thenReturn(proveedorEsperado);
//
//        List<Proveedor> proveedorActual = proveedorService.findAll(nombre, direccion);
//
//        assertIterableEquals(proveedorEsperado, proveedorActual);
//
//        verify(proveedorRepository, times(1)).getByNameAndAddressContainingIgnoreCase(nombre, direccion);
//
//    }
//
//    @Test
//    void save() {
//        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto(
//                "Proveedor Creado",
//                1,
//                "Calle 1"
//        );
//
//        Proveedor proveedorEsperado = Proveedor.builder()
//                .idProveedor(UUID.randomUUID())
//                .nombre(proveedorCreateDto.name())
//                .contacto(proveedorCreateDto.contact())
//                .direccion(proveedorCreateDto.address())
//                .fechaContratacion(LocalDate.now())
//                .build();
//
//        when(proveedorRepository.generateUUID()).thenReturn(proveedorEsperado.getIdProveedor());
//        when(proveedorRepository.save(proveedorEsperado)).thenReturn(proveedorEsperado);
//        when(proveedorMapper.toProveedor(proveedorCreateDto, proveedorEsperado.getIdProveedor())).thenReturn(proveedorEsperado);
//
//        Proveedor proveedorActual = proveedorService.save(proveedorCreateDto);
//
//        assertAll(
//                () -> assertEquals(proveedorEsperado.getIdProveedor(), proveedorActual.getIdProveedor()),
//                () -> assertEquals(proveedorEsperado.getName(), proveedorActual.getName()),
//                () -> assertEquals(proveedorEsperado.getContacto(), proveedorActual.getContacto()),
//                () -> assertEquals(proveedorEsperado.getDireccion(), proveedorActual.getDireccion()),
//                () -> assertEquals(proveedorEsperado.getFechaContratacion(), proveedorActual.getFechaContratacion())
//        );
//
//        verify(proveedorRepository, times(1)).generateUUID();
//        verify(proveedorRepository, times(1)).save(proveedorArgumentCaptor.capture());
//        verify(proveedorMapper, times(1)).toProveedor(proveedorCreateDto, proveedorEsperado.getIdProveedor());
//    }
//
//    @Test
//    void update() {
//        UUID uuid = UUID.randomUUID();
//        ProveedorUpdateDto proveedorUpdateDto = new ProveedorUpdateDto(
//                "Proveedor Actualizado",
//                "2",
//                "Calle 2"
//        );
//
//        Proveedor proveedor = proveedor1;
//
//        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor));
//        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);
//        when(proveedorMapper.toProveedor(proveedorUpdateDto, proveedor)).thenReturn(proveedor);
//
//        Proveedor proveedorActual = proveedorService.update(proveedorUpdateDto, uuid.toString());
//
//        assertAll(
//                () -> assertEquals(proveedor.getIdProveedor(), proveedorActual.getIdProveedor()),
//                () -> assertEquals(proveedor.getName(), proveedorActual.getName()),
//                () -> assertEquals(proveedor.getContacto(), proveedorActual.getContacto()),
//                () -> assertEquals(proveedor.getDireccion(), proveedorActual.getDireccion()),
//                () -> assertEquals(proveedor.getFechaContratacion(), proveedorActual.getFechaContratacion())
//        );
//
//        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
//        verify(proveedorRepository, times(1)).save(proveedorArgumentCaptor.capture());
//        verify(proveedorMapper, times(1)).toProveedor(proveedorUpdateDto, proveedor);
//    }
//
//    @Test
//    void deleteByUUID() {
//        UUID uuid = UUID.randomUUID();
//        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.of(proveedor1));
//        proveedorService.deleteByUUID(uuid.toString());
//        verify(proveedorRepository, times(1)).deleteByIdProveedor(uuid);
//    }
//
//    @Test
//    void deleteByUUIDNotFound() {
//        UUID uuid = UUID.randomUUID();
//        when(proveedorRepository.getByIdProveedor(uuid)).thenReturn(java.util.Optional.empty());
//        assertThrows(ProveedorNotFound.class, () -> proveedorService.deleteByUUID(uuid.toString()));
//        verify(proveedorRepository, times(1)).getByIdProveedor(uuid);
//    }
//}