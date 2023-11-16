package dev.clownsinformatics.tiendajava.rest.suppliers.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.categories.models.Category;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierCreateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierResponseDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.dto.SupplierUpdateDto;
import dev.clownsinformatics.tiendajava.rest.suppliers.exceptions.SupplierBadRequest;
import dev.clownsinformatics.tiendajava.rest.suppliers.exceptions.SupplierNotFound;
import dev.clownsinformatics.tiendajava.rest.suppliers.services.SupplierServiceImpl;
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
            "Supplier 1",
            1,
            "Calle 1",
            LocalDateTime.now(),
            category1
    );

    private final SupplierResponseDto supplierResponseDto2 = new SupplierResponseDto(
            UUID.randomUUID(),
            "Supplier 2",
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
        var supplierList = List.of(supplierResponseDto, supplierResponseDto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(supplierList);

        when(supplierService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(myEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> supplier = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, supplier.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByName() throws Exception {
        var supplierList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?name=Supplier 1";
        Optional<String> name = Optional.of("Supplier 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(supplierList);

        when(supplierService.findAll(Optional.empty(), name, Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> supplier = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, supplier.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), name, Optional.empty(), pageable);
    }

    @Test
    void getAllByCategory() throws Exception {
        var supplierList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?category=Category 1";
        Optional<String> categoryName = Optional.of("Category 1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(supplierList);

        when(supplierService.findAll(categoryName, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> supplier = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, supplier.content().size())
        );

        verify(supplierService, times(1)).findAll(categoryName, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllByContact() throws Exception {
        var supplierList = List.of(supplierResponseDto);
        var localEndpoint = myEndpoint + "?contact= 1";
        Optional<Integer> contactNumber = Optional.of(1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(supplierList);

        when(supplierService.findAll(Optional.empty(), Optional.empty(), contactNumber, pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<SupplierResponseDto> supplier = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, supplier.content().size())
        );

        verify(supplierService, times(1)).findAll(Optional.empty(), Optional.empty(), contactNumber, pageable);
    }

    @Test
    void getSupplierByUUID() throws Exception {
        String localEndpoint = myEndpoint + "/" + supplierResponseDto.id();
        when(supplierService.findByUUID(supplierResponseDto.id().toString())).thenReturn(supplierResponseDto);
        MockHttpServletResponse response = mockMvc.perform(
                get(localEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SupplierResponseDto supplierResponseDto1 = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), supplierResponseDto1.id()),
                () -> assertEquals(supplierResponseDto.name(), supplierResponseDto1.name()),
                () -> assertEquals(supplierResponseDto.contact(), supplierResponseDto1.contact()),
                () -> assertEquals(supplierResponseDto.address(), supplierResponseDto1.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), supplierResponseDto1.dateOfHire())
        );
    }

    @Test
    void getSupplierByUUIDNotFound() throws Exception {
        String localEndpoint = myEndpoint + "/" + supplierResponseDto.id();
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
    void getSupplierByUUIDBadRequest() throws Exception {
        String localEndpoint = myEndpoint + "/" + "not a uuid";
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
    void createSupplier() throws Exception {
        SupplierCreateDto supplierCreateDto = new SupplierCreateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.save(any(SupplierCreateDto.class))).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SupplierResponseDto supplier = mapper.readValue(response.getContentAsString(), SupplierResponseDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(supplierResponseDto.id(), supplier.id()),
                () -> assertEquals(supplierResponseDto.name(), supplier.name()),
                () -> assertEquals(supplierResponseDto.contact(), supplier.contact()),
                () -> assertEquals(supplierResponseDto.address(), supplier.address()),
                () -> assertEquals(supplierResponseDto.dateOfHire(), supplier.dateOfHire())
        );
    }

    @Test
    void supplierCreatedBadRequestName() throws Exception {
        var supplierCreateDto = new SupplierCreateDto(
                "",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.save(supplierCreateDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(supplierCreateDto);
    }

    @Test
    void supplierCreatedBadRequestContact() throws Exception {
        var supplierCreateDto = new SupplierCreateDto(
                "Supplier 1",
                -1,
                "Calle 1",
                category1
        );
        when(supplierService.save(supplierCreateDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(supplierCreateDto);
    }

    @Test
    void supplierCreatedBadRequestAddress() throws Exception {
        var supplierCreateDto = new SupplierCreateDto(
                "Supplier 1",
                1,
                "",
                category1
        );
        when(supplierService.save(supplierCreateDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(supplierCreateDto);
    }

    @Test
    void supplierCreatedBadRequestCategory() throws Exception {
        var supplierCreateDto = new SupplierCreateDto(
                "Supplier 1",
                1,
                "Calle 1",
                null
        );
        when(supplierService.save(supplierCreateDto)).thenThrow(new SupplierBadRequest(""));

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(supplierService, times(0)).save(supplierCreateDto);
    }

    @Test
    void updateSupplier() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        var supplierUpdateDto = new SupplierUpdateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category1
        );

        when(supplierService.update(supplierUpdateDto, uuid)).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierUpdateDto))
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

        verify(supplierService, times(1)).update(supplierUpdateDto, uuid);
    }

    @Test
    void updateSupplierNotFound() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        var supplierUpdateDto = new SupplierUpdateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category1
        );

        when(supplierService.update(supplierUpdateDto, uuid)).thenThrow(new SupplierNotFound(UUID.fromString(uuid)));

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(supplierUpdateDto))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(supplierService, times(1)).update(supplierUpdateDto, uuid);
    }

    @Test
    void updateSupplierPatch() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        var supplierUpdateDto = new SupplierUpdateDto(
                "Supplier 1",
                1,
                "Calle 1",
                category1
        );
        when(supplierService.update(supplierUpdateDto, uuid)).thenReturn(supplierResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(supplierUpdateDto))
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

        verify(supplierService, times(1)).update(supplierUpdateDto, uuid);
    }


    @Test
    void deleteSupplier() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/" + uuid;

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
    void deleteSupplierNotFound() throws Exception {
        var uuid = UUID.randomUUID().toString();
        var myLocalEndpoint = myEndpoint + "/" + uuid;

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