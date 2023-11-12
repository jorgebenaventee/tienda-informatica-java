package dev.clownsinformatics.tiendajava.rest.employees.services;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {
    Page<EmployeeResponseDto> findAll(Optional<String> name, Optional<Double> minSalary, Optional<Double> maxSalary, Optional<String> position, Pageable pageable);

    @CachePut(key = "#id")
    EmployeeResponseDto findById(Integer id);

    @CachePut(key = "#result.id")
    EmployeeResponseDto save(CreateEmployeeRequestDto createEmployeeRequestDto);

    @CachePut(key = "#id")
    EmployeeResponseDto update(Integer id, UpdateEmployeeRequestDto updateEmployeeRequestDto);

    @CacheEvict(key = "#id")
    void delete(Integer id);
}
