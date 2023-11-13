package dev.clownsinformatics.tiendajava.proveedores.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorBadRequest;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.ProveedorServiceImpl;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
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
class ProveedorControllerTest {
    private final String myEndpoint = "/api/suppliers";


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

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    ProveedorServiceImpl proveedorService;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    public ProveedorControllerTest(ProveedorServiceImpl proveedorService) {
        this.proveedorService = proveedorService;
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    void getAll() throws Exception {
        var proveedorList = List.of(proveedorResponseDto1, proveedorResponseDto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(proveedorService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(myEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ProveedorResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, proveedor.content().size())
        );

        verify(proveedorService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByName() throws Exception {
        var proveedorList = List.of(proveedorResponseDto1);
        var localEndpoint = myEndpoint + "?name=Proveedor 1";
        Optional<String> name = Optional.of("Proveedor 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(proveedorService.findAll(name, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ProveedorResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(proveedorService, times(1)).findAll(name, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByCategory() throws Exception {
        var proveedorList = List.of(proveedorResponseDto1);
        var localEndpoint = myEndpoint + "?category=Categoria 1";
        Optional<String> categoryName = Optional.of("Categoria 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(proveedorService.findAll(Optional.empty(), categoryName, Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ProveedorResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(proveedorService, times(1)).findAll(Optional.empty(), categoryName, Optional.empty(), pageable);
    }

    @Test
    void getAllByContact() throws Exception {
        var proveedorList = List.of(proveedorResponseDto1);
        var localEndpoint = myEndpoint + "?contact= 1";
        Optional<Integer> contactNumber = Optional.of(1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(proveedorService.findAll(Optional.empty(), Optional.empty(), contactNumber, pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<ProveedorResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(proveedorService, times(1)).findAll(Optional.empty(), Optional.empty(), contactNumber, pageable);
    }

    @Test
    void getProveedorByUUID() throws Exception {
        var localEndpoint = myEndpoint + "/" + proveedor1.getIdProveedor();
        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenReturn(proveedor1);
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        ProveedorResponseDto proveedorResponseDto = mapper.readValue(response.getContentAsString(), ProveedorResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(proveedor1.getIdProveedor(), proveedorResponseDto.idProveedor()),
                () -> assertEquals(proveedor1.getName(), proveedorResponseDto.name()),
                () -> assertEquals(proveedor1.getContact(), proveedorResponseDto.contact()),
                () -> assertEquals(proveedor1.getAddress(), proveedorResponseDto.address()),
                () -> assertEquals(proveedor1.getDateOfHire(), proveedorResponseDto.dateOfHire())
        );
    }

    @Test
    void getProveedorByUUIDNotFound() throws Exception {
        var localEndpoint = myEndpoint + "/" + proveedor1.getIdProveedor();
        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenThrow(new ProveedorNotFound(proveedor1.getIdProveedor()));
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(proveedorService, times(1)).findByUUID(proveedor1.getIdProveedor().toString());
    }

    @Test
    void getProveedorByUUIDBadRequest() throws Exception {
        var localEndpoint = myEndpoint + "/" + "a";
        when(proveedorService.findByUUID(proveedor1.getIdProveedor().toString())).thenThrow(new ProveedorBadRequest("123"));
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("a is not a valid UUID", response.getContentAsString())
        );
        verify(proveedorService, times(0)).findByUUID("123");
    }

    @Test
    void createProveedor() throws Exception {
        var proveedorDto = new ProveedorCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );
        when(proveedorService.save(any(ProveedorCreateDto.class))).thenReturn(proveedor1);

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ProveedorResponseDto proveedor = mapper.readValue(response.getContentAsString(), ProveedorResponseDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(proveedor1.getIdProveedor(), proveedor.idProveedor()),
                () -> assertEquals(proveedor1.getName(), proveedor.name()),
                () -> assertEquals(proveedor1.getContact(), proveedor.contact()),
                () -> assertEquals(proveedor1.getAddress(), proveedor.address()),
                () -> assertEquals(proveedor1.getDateOfHire(), proveedor.dateOfHire())
        );
    }

    @Test
    void proveedorCreatedBadRequestName() throws Exception {
        var proveedorDto = new ProveedorCreateDto(
                "",
                1,
                "Calle 1",
                category1
        );
        when(proveedorService.save(any(ProveedorCreateDto.class))).thenThrow(new ProveedorBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("The name cannot be blank", response.getContentAsString())
        );
    }

    @Test
    void proveedorCreatedBadRequestContact() throws Exception {
        var proveedorDto = new ProveedorCreateDto(
                "Proveedor 1",
                0,
                "Calle 1",
                category1
        );
        when(proveedorService.save(any(ProveedorCreateDto.class))).thenThrow(new ProveedorBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("The contact cannot be less than 0", response.getContentAsString())
        );
    }

    @Test
    void proveedorCreatedBadRequestAddress() throws Exception {
        var proveedorDto = new ProveedorCreateDto(
                "Proveedor 1",
                1,
                "",
                category1
        );
        when(proveedorService.save(any(ProveedorCreateDto.class))).thenThrow(new ProveedorBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("The address cannot be blank", response.getContentAsString())
        );
    }

    @Test
    void proveedorCreatedBadRequestCategory() throws Exception {
        var proveedorDto = new ProveedorCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                null
        );
        when(proveedorService.save(any(ProveedorCreateDto.class))).thenThrow(new ProveedorBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("The category cannot be null", response.getContentAsString())
        );
    }

    @Test
    void updateProveedor() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var proveedorDto = new ProveedorUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );

        when(proveedorService.update(any(ProveedorUpdateDto.class), UUID.randomUUID().toString())).thenReturn(proveedor1);

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ProveedorResponseDto funko = mapper.readValue(response.getContentAsString(), ProveedorResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(proveedor1.getIdProveedor(), funko.idProveedor()),
                () -> assertEquals(proveedor1.getName(), funko.name()),
                () -> assertEquals(proveedor1.getContact(), funko.contact()),
                () -> assertEquals(proveedor1.getAddress(), funko.address()),
                () -> assertEquals(proveedor1.getDateOfHire(), funko.dateOfHire())
        );

        verify(proveedorService, times(1)).update(any(ProveedorUpdateDto.class),  UUID.randomUUID().toString());
    }

    @Test
    void updateProveedorNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/99";
        var proveedorDto = new ProveedorUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );

        when(proveedorService.update(any(ProveedorUpdateDto.class), UUID.randomUUID().toString())).thenThrow(new ProveedorNotFound(UUID.randomUUID()));

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        verify(proveedorService, times(1)).update(any(ProveedorUpdateDto.class), UUID.randomUUID().toString());
    }

    @Test
    void updateProveedorPatch() {
    }

    @Test
    void deleteProveedor() {
    }

    @Test
    void handleValidationExceptions() {
    }
}