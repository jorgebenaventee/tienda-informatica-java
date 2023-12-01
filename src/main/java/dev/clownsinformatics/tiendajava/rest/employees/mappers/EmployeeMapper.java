package dev.clownsinformatics.tiendajava.rest.employees.mappers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre objetos de tipo {@link Employee} y {@link CreateEmployeeRequestDto}
 */
@Component
public class EmployeeMapper {
    /**
     * Convierte un objeto de tipo {@link CreateEmployeeRequestDto} a {@link Employee}
     * @param requestDto Objeto de tipo {@link CreateEmployeeRequestDto} a convertir
     * @return Objeto de tipo {@link Employee} convertido
     */
    public Employee toEmployee(CreateEmployeeRequestDto requestDto) {
        return Employee.builder()
                .name(requestDto.name())
                .salary(requestDto.salary())
                .position(requestDto.position())
                .build();
    }

    /**
     * Convierte un objeto de tipo {@link UpdateEmployeeRequestDto} a {@link Employee}
     * @param requestDto Objeto de tipo {@link UpdateEmployeeRequestDto} a convertir
     * @param employee Objeto de tipo {@link Employee} a actualizar
     * @return Objeto de tipo {@link Employee} convertido
     */
    public Employee toEmployee(UpdateEmployeeRequestDto requestDto, Employee employee) {
        return Employee.builder()
                .id(employee.getId())
                .name(requestDto.name() != null ? requestDto.name() : employee.getName())
                .salary(requestDto.salary() != null ? requestDto.salary() : employee.getSalary())
                .position(requestDto.position() != null ? requestDto.position() : employee.getPosition())
                .build();
    }

    /**
     * Convierte un objeto de tipo {@link Employee} a {@link EmployeeResponseDto}
     * @param employee Objeto de tipo {@link Employee} a convertir
     * @return Objeto de tipo {@link EmployeeResponseDto} convertido
     */
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
