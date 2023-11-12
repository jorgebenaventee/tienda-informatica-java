package dev.clownsinformatics.tiendajava.rest.employees.mappers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toEmployee(CreateEmployeeRequestDto requestDto) {
        return Employee.builder()
                .name(requestDto.name())
                .salary(requestDto.salary())
                .position(requestDto.position())
                .build();
    }

    public Employee toEmployee(UpdateEmployeeRequestDto requestDto, Employee employee) {
        return Employee.builder()
                .id(employee.getId())
                .name(requestDto.name() != null ? requestDto.name() : employee.getName())
                .salary(requestDto.salary() != null ? requestDto.salary() : employee.getSalary())
                .position(requestDto.position() != null ? requestDto.position() : employee.getPosition())
                .build();
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getPosition(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
