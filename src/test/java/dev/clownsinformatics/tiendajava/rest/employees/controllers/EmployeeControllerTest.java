package dev.clownsinformatics.tiendajava.rest.employees.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.exceptions.EmployeeNotFoundException;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import dev.clownsinformatics.tiendajava.rest.employees.services.EmployeeService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    private final String BASE_URL = "/api/employee";
    LocalDateTime now = LocalDateTime.now();
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;

    private Employee employee = Employee.builder()
            .id(1)
            .name("Pedro Pérez")
            .position("Manager")
            .salary(1000.0)
            .build();

    private Employee employee2 = Employee.builder()
            .id(2)
            .name("Juan Pérez")
            .position("Developer")
            .salary(1000.0)
            .build();

    private EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(1, "Pedro Pérez", 1000.0, "Manager", now, now);
    private EmployeeResponseDto employeeResponseDto2 = new EmployeeResponseDto(2, "Juan Pérez", 1500.0, "Developer", now, now);


    @Autowired
    public EmployeeControllerTest(EmployeeService employeeService) {
        this.employeeService = employeeService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllEmployees() throws Exception {
        List<EmployeeResponseDto> employees = List.of(employeeResponseDto, employeeResponseDto2);
        Page<EmployeeResponseDto> page = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(employeeService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<EmployeeResponseDto> body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, body.content().size())
        );

        verify(employeeService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllEmployeesByName() throws Exception {
        String localUrl = BASE_URL + "?name=Pedro Pérez";
        List<EmployeeResponseDto> employees = List.of(employeeResponseDto);
        Page<EmployeeResponseDto> page = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(employeeService.findAll(Optional.of("Pedro Pérez"), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<EmployeeResponseDto> body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.content().size()),
                () -> assertTrue(body.content().get(0).name().contains("Pedro"))
        );

        verify(employeeService, times(1)).findAll(Optional.of("Pedro Pérez"), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllEmployeesByMinSalary() throws Exception {
        String localUrl = BASE_URL + "?minSalary=1300";
        List<EmployeeResponseDto> employees = List.of(employeeResponseDto2);
        Page<EmployeeResponseDto> page = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(employeeService.findAll(Optional.empty(), Optional.of(1300.0), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<EmployeeResponseDto> body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.content().size()),
                () -> assertTrue(body.content().get(0).salary() >= 1300.0)
        );

        verify(employeeService, times(1)).findAll(Optional.empty(), Optional.of(1300.0), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllEmployeesByMaxSalary() throws Exception {
        String localUrl = BASE_URL + "?maxSalary=1300";
        List<EmployeeResponseDto> employees = List.of(employeeResponseDto);
        Page<EmployeeResponseDto> page = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(employeeService.findAll(Optional.empty(), Optional.empty(), Optional.of(1300.0), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<EmployeeResponseDto> body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.content().size()),
                () -> assertTrue(body.content().get(0).salary() <= 1300.0)
        );

        verify(employeeService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.of(1300.0), Optional.empty(), pageable);
    }

    @Test
    void getEmployeeById() throws Exception {
        String localUrl = BASE_URL + "/1";

        when(employeeService.findById(1)).thenReturn(employeeResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        EmployeeResponseDto body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {

        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.id())
        );
    }

    @Test
    void getEmployeeByIdNotExists() throws Exception {
        String localUrl = BASE_URL + "/1";

        when(employeeService.findById(1)).thenThrow(new EmployeeNotFoundException(1));

        MockHttpServletResponse response = mockMvc.perform(
                        get(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
    }

    @Test
    void createEmployee() throws Exception {
        String localUrl = BASE_URL;

        when(employeeService.save(any())).thenReturn(employeeResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        EmployeeResponseDto body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.id())
        );
    }

    @Test
    void createEmployeeWithBlankName() throws Exception {
        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("", 1000.0, "Manager");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The name must be between 3 and 50 characters"))
        );
    }


    @Test
    void createEmployeeWithShortName() throws Exception {
        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pe", 1000.0, "Manager");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The name must be between 3 and 50 characters"))
        );
    }

    @Test
    void createEmployeeWithNullSalary() throws Exception {

        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pepe", null, "Manager");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The salary cannot be null"))
        );
    }


    @Test
    void createEmployeeWithNegativeSalary() throws Exception {

        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pepe", -1000.0, "Manager");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The salary must be greater than or equal to 0"))
        );
    }

    @Test
    void createEmployeeWithBlankPosition() throws Exception {

        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pepe", 1000.0, "");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The position cannot be blank"))
        );
    }

    @Test
    void createEmployeeWithShortPosition() throws Exception {

        String localUrl = BASE_URL;

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pepe", 1000.0, "Ma");

        MockHttpServletResponse response = mockMvc.perform(
                        post(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The position must be between 3 and 50 characters"))
        );
    }

    @Test
    void updateEmployee() throws Exception {
        String localUrl = BASE_URL + "/1";
        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", 3000.0, "Manager Senior");
        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager Senior", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);


        MockHttpServletResponse response = mockMvc.perform(
                        put(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        EmployeeResponseDto body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.id()),
                () -> assertEquals("Pedrito", body.name()),
                () -> assertEquals(3000.0, body.salary()),
                () -> assertEquals("Manager Senior", body.position())
        );
    }

    @Test
    void updateEmployeeWithShortName() throws Exception {
        String localUrl = BASE_URL + "/1";
        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pe", 3000.0, null);
        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        put(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The name must be between 3 and 50 characters"))
        );
    }

    @Test
    void updateEmployeeWithNegativeSalary() throws Exception {
        String localUrl = BASE_URL + "/1";

        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", -3000.0, null);

        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        put(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The salary must be greater than or equal to 0"))
        );
    }


    @Test
    void updateEmployeeWithShortPosition() throws Exception {
        String localUrl = BASE_URL + "/1";

        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", 3000.0, "Ma");

        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        put(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The position must be between 3 and 50 characters"))
        );
    }

    // Partial update is the same as update but using patch
    @Test
    void partialUpdateEmployee() throws Exception {
        String localUrl = BASE_URL + "/1";
        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", 3000.0, null);
        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);


        MockHttpServletResponse response = mockMvc.perform(
                        patch(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        EmployeeResponseDto body = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, body.id()),
                () -> assertEquals("Pedrito", body.name()),
                () -> assertEquals(3000.0, body.salary()),
                () -> assertEquals("Manager", body.position())
        );

    }

    @Test
    void partialUpdateEmployeeWithShortName() throws Exception {
        String localUrl = BASE_URL + "/1";
        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pe", 3000.0, null);
        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The name must be between 3 and 50 characters"))
        );
    }

    @Test
    void partialUpdateEmployeeWithNegativeSalary() throws Exception {
        String localUrl = BASE_URL + "/1";

        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", -3000.0, null);

        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The salary must be greater than or equal to 0"))
        );
    }

    @Test
    void partialUpdateEmployeeWithShortPosition() throws Exception {
        String localUrl = BASE_URL + "/1";

        UpdateEmployeeRequestDto employee = new UpdateEmployeeRequestDto("Pedrito", 3000.0, "Ma");

        EmployeeResponseDto newEmployee = new EmployeeResponseDto(1, "Pedrito", 3000.0, "Manager", now, now);

        when(employeeService.update(anyInt(), any(UpdateEmployeeRequestDto.class))).thenReturn(newEmployee);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(employee)))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(body.contains("The position must be between 3 and 50 characters"))
        );
    }

    @Test
    void deleteEmployee() throws Exception {
        String localUrl = BASE_URL + "/1";

        doNothing().when(employeeService).delete(1);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
    }

    @Test
    void deleteEmployeeNotExists() throws Exception {
        String localUrl = BASE_URL + "/1";

        doThrow(new EmployeeNotFoundException(1)).when(employeeService).delete(1);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(localUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String body = response.getContentAsString();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
    }
}