package dev.clownsinformatics.tiendajava.proveedores.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.SupplierBadRequest;
import dev.clownsinformatics.tiendajava.rest.proveedores.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.proveedores.services.SupplierServiceImpl;
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
class SupplierControllerTest {
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

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    SupplierServiceImpl supplierService;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    public SupplierControllerTest(SupplierServiceImpl supplierService) {
        this.supplierService = supplierService;
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    void getAll() throws Exception {
        var proveedorList = List.of(supplierResponseDto, supplierResponseDto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(supplierService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(myEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, proveedor.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByName() throws Exception {
        var proveedorList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?name=Proveedor 1";
        Optional<String> name = Optional.of("Proveedor 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(supplierService.findAll(Optional.empty(), name, Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), name, Optional.empty(), pageable);
    }

    @Test
    void getAllByCategory() throws Exception {
        var proveedorList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?category=Category 1";
        Optional<String> categoryName = Optional.of("Category 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(supplierService.findAll(categoryName, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(supplierService, times(1)).findAll(categoryName, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByContact() throws Exception {
        var proveedorList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?contact= 1";
        Optional<Integer> contactNumber = Optional.of(1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(proveedorList);

        when(supplierService.findAll(Optional.empty(), Optional.empty(), contactNumber, pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> proveedor = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, proveedor.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), Optional.empty(), contactNumber, pageable);
    }

    @Test
    void getProveedorByUUID() throws Exception {
        var localEndpoint = myEndpoint + "/" + supplierResponseDto.id();
        when(supplierService.findByUUID(supplierResponseDto.id().toString())).thenReturn(supplierResponseDto);
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        SupplierResponseDto proveedorResponseDto = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), proveedorResponseDto.id()),
                () -> assertEquals(supplierResponseDto.name(), proveedorResponseDto.name()),
                () -> assertEquals(supplierResponseDto.contact(), proveedorResponseDto.contact()),
                () -> assertEquals(supplierResponseDto.address(), proveedorResponseDto.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), proveedorResponseDto.dateOfHire())
        );
    }

    @Test
    void getProveedorByUUIDNotFound() throws Exception {
        var localEndpoint = myEndpoint + "/" + supplierResponseDto.id();
        when(supplierService.findByUUID(supplierResponseDto.id().toString())).thenThrow(new SupplierNotFound(supplierResponseDto.id()));
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(supplierService, times(1)).findByUUID(supplierResponseDto.id().toString());
    }

    @Test
    void getProveedorByUUIDBadRequest() throws Exception {
        var localEndpoint = myEndpoint + "/" + "not a uuid";
        when(supplierService.findByUUID("not a uuid")).thenThrow(new SupplierBadRequest("not a uuid is not a valid UUID"));
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(1)).findByUUID("not a uuid");
    }

    @Test
    void createProveedor() throws Exception {
        var proveedorDto = new SupplierCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.save(any(SupplierCreateDto.class))).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SupplierResponseDto proveedor = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), proveedor.id()),
                () -> assertEquals(supplierResponseDto.name(), proveedor.name()),
                () -> assertEquals(supplierResponseDto.contact(), proveedor.contact()),
                () -> assertEquals(supplierResponseDto.address(), proveedor.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), proveedor.dateOfHire())
        );
    }

    @Test
    void proveedorCreatedBadRequestName() throws Exception {
        var proveedorDto = new SupplierCreateDto(
                "",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.save(proveedorDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(proveedorDto);
    }

    @Test
    void proveedorCreatedBadRequestContact() throws Exception {
        var proveedorDto = new SupplierCreateDto(
                "Proveedor 1",
                -1,
                "Calle 1",
                category1
        );
        when(supplierService.save(proveedorDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(proveedorDto);
    }

    @Test
    void proveedorCreatedBadRequestAddress() throws Exception {
        var proveedorDto = new SupplierCreateDto(
                "Proveedor 1",
                1,
                "",
                category1
        );
        when(supplierService.save(proveedorDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(proveedorDto);
    }

    @Test
    void proveedorCreatedBadRequestCategory() throws Exception {
        var proveedorDto = new SupplierCreateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                null
        );
        when(supplierService.save(proveedorDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(proveedorDto);
    }

    @Test
    void updateProveedor() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/"+uuid;
        var proveedorDto = new SupplierUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );

        when(supplierService.update(proveedorDto,uuid)).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SupplierResponseDto supplier = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), supplier.id()),
                () -> assertEquals(supplierResponseDto.name(), supplier.name()),
                () -> assertEquals(supplierResponseDto.contact(), supplier.contact()),
                () -> assertEquals(supplierResponseDto.address(), supplier.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), supplier.dateOfHire())
        );

      verify(supplierService, times(1)).update(proveedorDto, uuid);
    }

    @Test
    void updateProveedorNotFound() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/"+uuid;
        var proveedorDto = new SupplierUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );

        when(supplierService.update(proveedorDto, uuid)).thenThrow(new SupplierNotFound(UUID.fromString(uuid)));

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(proveedorDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(supplierService, times(1)).update(proveedorDto, uuid);
    }

    @Test
    void updateProveedorPatch() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/"+uuid;
        var proveedorDto = new SupplierUpdateDto(
                "Proveedor 1",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.update(proveedorDto, uuid)).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(proveedorDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        SupplierResponseDto supplier = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), supplier.id()),
                () -> assertEquals(supplierResponseDto.name(), supplier.name()),
                () -> assertEquals(supplierResponseDto.contact(), supplier.contact()),
                () -> assertEquals(supplierResponseDto.address(), supplier.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), supplier.dateOfHire())
        );

        verify(supplierService, times(1)).update(proveedorDto, uuid);
    }


    @Test
    void deleteProveedor() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/"+uuid;

        doNothing().when(supplierService).deleteByUUID(uuid);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(supplierService, times(1)).deleteByUUID(uuid);
    }

    @Test
    void deleteProveedorNotFound() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/"+uuid;

        doThrow(new SupplierNotFound(UUID.fromString(uuid))).when(supplierService).deleteByUUID(uuid);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        verify(supplierService, times(1)).deleteByUUID(uuid);
    }
}