package dev.clownsinformatics.tiendajava.rest.employees.controllers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.exceptions.EmployeeNotFoundException;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import dev.clownsinformatics.tiendajava.rest.employees.services.EmployeeService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    LocalDateTime now = LocalDateTime.now();
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
    @Mock
    private EmployeeService employeeService;
    @Mock
    private PaginationLinksUtils paginationLinksUtils;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployees() {
        Optional<String> name = Optional.empty();
        Optional<Double> minSalary = Optional.empty();
        Optional<Double> maxSalary = Optional.empty();
        Optional<String> position = Optional.empty();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<EmployeeResponseDto> employeeResponseDtoList = List.of(employeeResponseDto, employeeResponseDto2);
        Page<EmployeeResponseDto> page = new PageImpl<>(employeeResponseDtoList);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/employee"));
        when(employeeService.findAll(name, minSalary, maxSalary, position, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<EmployeeResponseDto>> allEmployees = employeeController.getAllEmployees(name, minSalary, maxSalary, position, 0, 10, "id", "asc", request);

        assertAll(
                () -> assertEquals(200, allEmployees.getStatusCodeValue()),
                () -> assertEquals(2, allEmployees.getBody().content().size()),
                () -> assertEquals(1, allEmployees.getBody().content().get(0).id())
        );
    }

    @Test
    void getAllByName() {
        Optional<String> name = Optional.of("Pedro");
        Optional<Double> minSalary = Optional.empty();
        Optional<Double> maxSalary = Optional.empty();
        Optional<String> position = Optional.empty();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        List<EmployeeResponseDto> employeeResponseDtoList = List.of(employeeResponseDto);
        Page<EmployeeResponseDto> page = new PageImpl<>(employeeResponseDtoList);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/employee"));
        when(employeeService.findAll(name, minSalary, maxSalary, position, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<EmployeeResponseDto>> allEmployees = employeeController.getAllEmployees(name, minSalary, maxSalary, position, 0, 10, "id", "asc", request);

        assertAll(
                () -> assertEquals(200, allEmployees.getStatusCodeValue()),
                () -> assertEquals(1, allEmployees.getBody().content().size()),
                () -> assertEquals(1, allEmployees.getBody().content().get(0).id())
        );
    }

    @Test
    void getAllByMinSalary() {
        Optional<String> name = Optional.empty();
        Optional<Double> minSalary = Optional.of(1000.0);
        Optional<Double> maxSalary = Optional.empty();
        Optional<String> position = Optional.empty();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        List<EmployeeResponseDto> employeeResponseDtoList = List.of(employeeResponseDto, employeeResponseDto2);
        Page<EmployeeResponseDto> page = new PageImpl<>(employeeResponseDtoList);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/employee"));
        when(employeeService.findAll(name, minSalary, maxSalary, position, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<EmployeeResponseDto>> allEmployees = employeeController.getAllEmployees(name, minSalary, maxSalary, position, 0, 10, "id", "asc", request);

        assertAll(
                () -> assertEquals(200, allEmployees.getStatusCodeValue()),
                () -> assertEquals(2, allEmployees.getBody().content().size()),
                () -> assertEquals(1, allEmployees.getBody().content().get(0).id())
        );
    }

    @Test
    void getAllByMaxSalary() {
        Optional<String> name = Optional.empty();
        Optional<Double> minSalary = Optional.empty();
        Optional<Double> maxSalary = Optional.of(1300.0);
        Optional<String> position = Optional.empty();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        List<EmployeeResponseDto> employeeResponseDtoList = List.of(employeeResponseDto);
        Page<EmployeeResponseDto> page = new PageImpl<>(employeeResponseDtoList);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/employee"));
        when(employeeService.findAll(name, minSalary, maxSalary, position, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<EmployeeResponseDto>> allEmployees = employeeController.getAllEmployees(name, minSalary, maxSalary, position, 0, 10, "id", "asc", request);

        assertAll(
                () -> assertEquals(200, allEmployees.getStatusCodeValue()),
                () -> assertEquals(1, allEmployees.getBody().content().size()),
                () -> assertEquals(1, allEmployees.getBody().content().get(0).id())
        );
    }


    @Test
    void getAllByPosition() {
        Optional<String> name = Optional.empty();
        Optional<Double> minSalary = Optional.empty();
        Optional<Double> maxSalary = Optional.empty();
        Optional<String> position = Optional.of("Manager");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        List<EmployeeResponseDto> employeeResponseDtoList = List.of(employeeResponseDto);
        Page<EmployeeResponseDto> page = new PageImpl<>(employeeResponseDtoList);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/employee"));
        when(employeeService.findAll(name, minSalary, maxSalary, position, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<EmployeeResponseDto>> allEmployees = employeeController.getAllEmployees(name, minSalary, maxSalary, position, 0, 10, "id", "asc", request);

        assertAll(
                () -> assertEquals(200, allEmployees.getStatusCodeValue()),
                () -> assertEquals(1, allEmployees.getBody().content().size()),
                () -> assertEquals(1, allEmployees.getBody().content().get(0).id())
        );
    }

    @Test
    void findById() {
        when(employeeService.findById(1)).thenReturn(employeeResponseDto);

        ResponseEntity<EmployeeResponseDto> employeeResponseDtoResponseEntity = employeeController.getEmployeeById(1);

        assertAll(
                () -> assertEquals(200, employeeResponseDtoResponseEntity.getStatusCodeValue()),
                () -> assertEquals(1, employeeResponseDtoResponseEntity.getBody().id())
        );
    }


    @Test
    void findByIdNotExists() {
        when(employeeService.findById(1)).thenThrow(new EmployeeNotFoundException(1));

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(1));

        assertEquals("Employee with id 1 not found", exception.getMessage());
    }

    @Test
    void createEmployee() {
        when(employeeService.save(any())).thenReturn(employeeResponseDto);

        CreateEmployeeRequestDto employee = new CreateEmployeeRequestDto("Pedro Pérez", 1000.0, "Manager");

        ResponseEntity<EmployeeResponseDto> employeeResponseDtoResponseEntity = employeeController.createEmployee(employee);

        assertAll(
                () -> assertEquals(200, employeeResponseDtoResponseEntity.getStatusCode().value()),
                () -> assertEquals(1, employeeResponseDtoResponseEntity.getBody().id())
        );
    }

    @Test
    void updateEmployee() {
        when(employeeService.update(anyInt(), any())).thenReturn(employeeResponseDto);
        UpdateEmployeeRequestDto employeeRequestDto = new UpdateEmployeeRequestDto("Pedro Pérez", 1000.0, "Manager");

        ResponseEntity<EmployeeResponseDto> employeeResponseDtoResponseEntity = employeeController.updateEmployee(1, employeeRequestDto);

        assertAll(
                () -> assertEquals(200, employeeResponseDtoResponseEntity.getStatusCode().value()),
                () -> assertEquals(1, employeeResponseDtoResponseEntity.getBody().id())
        );
    }

    @Test
    void updateEmployeeNotExists() {
        when(employeeService.update(anyInt(), any())).thenThrow(new EmployeeNotFoundException(1));
        UpdateEmployeeRequestDto employeeRequestDto = new UpdateEmployeeRequestDto("Pedro Pérez", 1000.0, "Manager");

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.updateEmployee(1, employeeRequestDto));

        assertEquals("Employee with id 1 not found", exception.getMessage());
    }

    @Test
    void partialUpdateEmployee() {
        when(employeeService.update(anyInt(), any())).thenReturn(employeeResponseDto);
        UpdateEmployeeRequestDto employeeRequestDto = new UpdateEmployeeRequestDto("Pedro Pérez", 1000.0, "Manager");

        ResponseEntity<EmployeeResponseDto> employeeResponseDtoResponseEntity = employeeController.updateEmployee(1, employeeRequestDto);

        assertAll(
                () -> assertEquals(200, employeeResponseDtoResponseEntity.getStatusCode().value()),
                () -> assertEquals(1, employeeResponseDtoResponseEntity.getBody().id())
        );
    }

    @Test
    void partialUpdateEmployeeNotExists() {
        when(employeeService.update(anyInt(), any())).thenThrow(new EmployeeNotFoundException(1));
        UpdateEmployeeRequestDto employeeRequestDto = new UpdateEmployeeRequestDto("Pedro Pérez", 1000.0, "Manager");

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.partialUpdateEmployee(1, employeeRequestDto));

        assertEquals("Employee with id 1 not found", exception.getMessage());
    }

    @Test
    void deleteEmployee() {
        doNothing().when(employeeService).delete(anyInt());
        ResponseEntity<Void> employeeResponseDtoResponseEntity = employeeController.deleteEmployee(1);

        assertEquals(204, employeeResponseDtoResponseEntity.getStatusCode().value());
    }


    @Test
    void deleteEmployeeNotExists() {
        doThrow(new EmployeeNotFoundException(1)).when(employeeService).delete(anyInt());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.deleteEmployee(1));

        assertEquals("Employee with id 1 not found", exception.getMessage());
    }
}
