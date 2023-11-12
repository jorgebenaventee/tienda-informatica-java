package dev.clownsinformatics.tiendajava.rest.employees.services;

import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

public interface EmployeeService {
    @CachePut(key = "#id")
    EmployeeResponseDto findById(Integer id);

    @CachePut(key = "#result.id")
    EmployeeResponseDto save(EmployeeRequestDto employeeRequestDto);

    @CachePut(key = "#id")
    EmployeeResponseDto update(Integer id, EmployeeRequestDto employeeRequestDto);

    @CacheEvict(key = "#id")
    void delete(Integer id);
}
