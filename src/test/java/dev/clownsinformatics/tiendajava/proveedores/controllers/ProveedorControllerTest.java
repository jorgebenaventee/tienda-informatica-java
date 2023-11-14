//package dev.clownsinformatics.tiendajava.proveedores.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorResponseDto;
//import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorBadRequest;
//import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
//import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
//import dev.clownsinformatics.tiendajava.rest.proveedores.services.ProveedorServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureJsonTesters
//@ExtendWith(MockitoExtension.class)
//class ProveedorControllerTest {
//    private final String myEndpoint = "/proveedor";
//
//    private final Proveedor proveedor1 = Proveedor.builder()
//            .idProveedor(UUID.randomUUID())
//            .nombre("Proveedor 1")
//            .contacto(1)
//            .direccion("Direccion 1")
//            .fechaContratacion(LocalDate.now())
//            .build();
//    private final Proveedor proveedor2 = Proveedor.builder()
//            .idProveedor(UUID.randomUUID())
//            .nombre("Proveedor 2")
//            .contacto(2)
//            .direccion("Direccion 2")
//            .fechaContratacion(LocalDate.now())
//            .build();
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @MockBean
//    ProveedorServiceImpl proveedorService;
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    public ProveedorControllerTest(ProveedorServiceImpl proveedorService) {
//        this.proveedorService = proveedorService;
//        mapper.registerModule(new JavaTimeModule());
//    }
//
//
//    @Test
//    void getAll() throws Exception {
//        var listaProveedores = List.of(proveedor1, proveedor2);
//        when(proveedorService.findAll(null, null)).thenReturn(listaProveedores);
//        MockHttpServletResponse response = mockMvc.perform(
//                get(myEndpoint)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn().getResponse();
//        List<ProveedorResponseDto> proveedores = mapper.readValue(response.getContentAsString(),
//                mapper.getTypeFactory().constructCollectionType(List.class, ProveedorResponseDto.class));
//        assertAll(
//                () -> assertEquals(200, response.getStatus()),
//                () -> assertEquals(proveedor1.getIdProveedor(), proveedores.get(0).idProveedor()),
//                () -> assertEquals(proveedor1.getName(), proveedores.get(0).name()),
//                () -> assertEquals(proveedor1.getContacto(), proveedores.get(0).contact()),
//                () -> assertEquals(proveedor1.getDireccion(), proveedores.get(0).address()),
//                () -> assertEquals(proveedor1.getFechaContratacion(), proveedores.get(0).dateOfHire()),
//                () -> assertEquals(proveedor2.getIdProveedor(), proveedores.get(1).idProveedor()),
//                () -> assertEquals(proveedor2.getName(), proveedores.get(1).name()),
//                () -> assertEquals(proveedor2.getContacto(), proveedores.get(1).contact()),
//                () -> assertEquals(proveedor2.getDireccion(), proveedores.get(1).address()),
//                () -> assertEquals(proveedor2.getFechaContratacion(), proveedores.get(1).dateOfHire())
//        );
//        verify(proveedorService, times(1)).findAll(null, null);
//    }
//
//    @Test
//    void getProveedorByUUID() throws Exception {
//        var localEndpoint = myEndpoint + "/" + proveedor1.getIdProveedor();
//        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenReturn(proveedor1);
//        MockHttpServletResponse response = mockMvc.perform(
//                get(localEndpoint)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn().getResponse();
//        ProveedorResponseDto proveedorResponseDto = mapper.readValue(response.getContentAsString(), ProveedorResponseDto.class);
//
//        assertAll(
//                () -> assertEquals(200, response.getStatus()),
//                () -> assertEquals(proveedor1.getIdProveedor(), proveedorResponseDto.idProveedor()),
//                () -> assertEquals(proveedor1.getName(), proveedorResponseDto.name()),
//                () -> assertEquals(proveedor1.getContacto(), proveedorResponseDto.contact()),
//                () -> assertEquals(proveedor1.getDireccion(), proveedorResponseDto.address()),
//                () -> assertEquals(proveedor1.getFechaContratacion(), proveedorResponseDto.dateOfHire())
//        );
//    }
//
//    @Test
//    void getProveedorByUUIDNotFound() throws Exception {
//        var localEndpoint = myEndpoint + "/" + proveedor1.getIdProveedor();
//        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenThrow(new ProveedorNotFound(proveedor1.getIdProveedor()));
//        MockHttpServletResponse response = mockMvc.perform(
//                get(localEndpoint)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn().getResponse();
//        assertAll(
//                () -> assertEquals(404, response.getStatus())
//        );
//        verify(proveedorService, times(1)).findByUUID(proveedor1.getIdProveedor().toString());
//    }
//
//    @Test
//    void getProveedorByUUIDBadRequest() throws Exception {
//        var localEndpoint = myEndpoint + "/" + "a";
//        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenThrow(new ProveedorBadRequest("123"));
//        MockHttpServletResponse response = mockMvc.perform(
//                get(localEndpoint)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn().getResponse();
//        assertAll(
//                () -> assertEquals(400, response.getStatus())
//        );
//        verify(proveedorService, times(0)).findByUUID("123");
//    }
//
//    @Test
//    void createProveedor() {
//
//    }
//
//    @Test
//    void updateProveedor() {
//    }
//
//    @Test
//    void updateProveedorPatch() {
//    }
//
//    @Test
//    void deleteProveedor() {
//    }
//
//    @Test
//    void handleValidationExceptions() {
//    }
//}