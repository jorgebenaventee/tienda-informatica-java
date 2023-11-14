package dev.clownsinformatics.tiendajava.rest.employees.services;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.exceptions.EmployeeNotFoundException;
import dev.clownsinformatics.tiendajava.rest.employees.mappers.EmployeeMapper;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import dev.clownsinformatics.tiendajava.rest.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    LocalDateTime now = LocalDateTime.now();
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    List<Employee> employees = List.of(
            Employee.builder().id(1).name("Juan").salary(1000.0).position("Developer").build(),
            Employee.builder().id(2).name("Pedro").salary(2000.0).position("Developer").build(),
            Employee.builder().id(3).name("David").salary(3000.0).position("Manager").build(),
            Employee.builder().id(4).name("Eva").salary(4000.0).position("Manager").build()
    );

    List<EmployeeResponseDto> employeeResponseDtos = List.of(
            new EmployeeResponseDto(1, "Juan", 1000.0, "Developer", now, now),
            new EmployeeResponseDto(2, "Pedro", 2000.0, "Developer", now, now),
            new EmployeeResponseDto(3, "David", 3000.0, "Manager", now, now),
            new EmployeeResponseDto(4, "Eva", 4000.0, "Manager", now, now)
    );


    @Test
    void findAll_NoParameters() {
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0), employeeResponseDtos.get(1), employeeResponseDtos.get(2), employeeResponseDtos.get(3));

        Page<EmployeeResponseDto> result = employeeService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertEquals(4, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(4, result.getContent().size()),
                () -> assertEquals(1, result.getContent().get(0).id()),
                () -> assertEquals(2, result.getContent().get(1).id()),
                () -> assertEquals(3, result.getContent().get(2).id()),
                () -> assertEquals(4, result.getContent().get(3).id())
        );

        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, times(4)).toResponseDto(any(Employee.class));
    }

    @Test
    void findAll_NameParameter() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employees.get(0)));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0));

        Page<EmployeeResponseDto> result = employeeService.findAll(Optional.of("Juan"), Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(1, result.getContent().get(0).id())
        );

        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, times(1)).toResponseDto(any(Employee.class));
    }


    @Test
    void findAll_MinSalaryParameter() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employees.get(2), employees.get(3)));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(2), employeeResponseDtos.get(3));

        Page<EmployeeResponseDto> result = employeeService.findAll(Optional.empty(), Optional.of(3000.0), Optional.empty(), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(3, result.getContent().get(0).id()),
                () -> assertEquals(4, result.getContent().get(1).id())
        );

        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, times(2)).toResponseDto(any(Employee.class));
    }

    @Test
    void findAll_MaxSalaryParameter() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employees.get(0), employees.get(1)));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0), employeeResponseDtos.get(1));

        Page<EmployeeResponseDto> result = employeeService.findAll(Optional.empty(), Optional.empty(), Optional.of(2000.0), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(1, result.getContent().get(0).id()),
                () -> assertEquals(2, result.getContent().get(1).id())
        );

        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, times(2)).toResponseDto(any(Employee.class));
    }

    @Test
    void findAll_PositionParameter() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employees.get(0), employees.get(1)));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0), employeeResponseDtos.get(1));

        Page<EmployeeResponseDto> result = employeeService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("Developer"), Pageable.unpaged());

        assertAll(
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(1, result.getContent().get(0).id()),
                () -> assertEquals(2, result.getContent().get(1).id())
        );

        verify(employeeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, times(2)).toResponseDto(any(Employee.class));
    }

    @Test
    void findById() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employees.get(0)));
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0));

        EmployeeResponseDto result = employeeService.findById(1);

        assertAll(
                () -> assertEquals(1, result.id()),
                () -> assertEquals("Juan", result.name()),
                () -> assertEquals(1000.0, result.salary()),
                () -> assertEquals("Developer", result.position())
        );

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void findById_NotExists() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(1));
        assertEquals("Employee with id 1 not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void save() {
        when(employeeMapper.toEmployee(any(CreateEmployeeRequestDto.class))).thenReturn(employees.get(0));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employees.get(0));
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0));

        EmployeeResponseDto result = employeeService.save(new CreateEmployeeRequestDto("Juan", 1000.0, "Developer"));

        assertAll(
                () -> assertEquals(1, result.id()),
                () -> assertEquals("Juan", result.name()),
                () -> assertEquals(1000.0, result.salary()),
                () -> assertEquals("Developer", result.position())
        );

        verify(employeeMapper, times(1)).toEmployee(any(CreateEmployeeRequestDto.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponseDto(any(Employee.class));
    }

    @Test
    void update() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employees.get(0)));
        when(employeeMapper.toEmployee(any(UpdateEmployeeRequestDto.class), any(Employee.class))).thenReturn(employees.get(0));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employees.get(0));
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponseDtos.get(0));

        EmployeeResponseDto result = employeeService.update(1, new UpdateEmployeeRequestDto("Juan", 1000.0, "Developer"));

        assertAll(
                () -> assertEquals(1, result.id()),
                () -> assertEquals("Juan", result.name()),
                () -> assertEquals(1000.0, result.salary()),
                () -> assertEquals("Developer", result.position())
        );

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, times(1)).toEmployee(any(UpdateEmployeeRequestDto.class), any(Employee.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateNotExists() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(1, new UpdateEmployeeRequestDto("Juan", 1000.0, "Developer")));
        assertEquals("Employee with id 1 not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void delete() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employees.get(0)));
        doNothing().when(employeeRepository).deleteById(1);

        employeeService.delete(1);

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_NotExists() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> employeeService.delete(1));
        assertEquals("Employee with id 1 not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
    }
}