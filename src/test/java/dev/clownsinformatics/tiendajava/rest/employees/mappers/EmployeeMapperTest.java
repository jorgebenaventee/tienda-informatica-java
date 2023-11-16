package dev.clownsinformatics.tiendajava.rest.employees.mappers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    void toEmployeeFromCreate() {
        CreateEmployeeRequestDto createEmployeeRequestDto = new CreateEmployeeRequestDto("Peter el Piedras", 1000.0, "Manager");
        assertAll(
                () -> assertEquals("Peter el Piedras", employeeMapper.toEmployee(createEmployeeRequestDto).getName()),
                () -> assertEquals(1000.0, employeeMapper.toEmployee(createEmployeeRequestDto).getSalary()),
                () -> assertEquals("Manager", employeeMapper.toEmployee(createEmployeeRequestDto).getPosition())
        );
    }

    @Test
    void toEmployeeFromUpdate() {
        UpdateEmployeeRequestDto updateEmployeeRequestDto = new UpdateEmployeeRequestDto("Peter el Piedras", 1000.0, "Manager");
        Employee employee = Employee.builder().id(1).name("Peter el Piedras").salary(1000.0).position("Manager").build();
        assertAll(
                () -> assertEquals("Peter el Piedras", employeeMapper.toEmployee(updateEmployeeRequestDto, employee).getName()),
                () -> assertEquals(1000.0, employeeMapper.toEmployee(updateEmployeeRequestDto, employee).getSalary()),
                () -> assertEquals("Manager", employeeMapper.toEmployee(updateEmployeeRequestDto, employee).getPosition())
        );
    }

    @Test
    void toResponseDto() {
        Employee employee = Employee.builder().id(1).name("Peter el Piedras").salary(1000.0).position("Manager").build();
        assertAll(
                () -> assertEquals(1, employeeMapper.toResponseDto(employee).id()),
                () -> assertEquals("Peter el Piedras", employeeMapper.toResponseDto(employee).name()),
                () -> assertEquals(1000.0, employeeMapper.toResponseDto(employee).salary()),
                () -> assertEquals("Manager", employeeMapper.toResponseDto(employee).position())
        );
    }
}