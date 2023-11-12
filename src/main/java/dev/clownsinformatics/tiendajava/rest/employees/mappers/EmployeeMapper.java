package dev.clownsinformatics.tiendajava.rest.employees.mappers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toEmployee(EmployeeRequestDto requestDto) {
        return Employee.builder()
                .name(requestDto.name())
                .salary(requestDto.salary())
                .position(requestDto.position())
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
