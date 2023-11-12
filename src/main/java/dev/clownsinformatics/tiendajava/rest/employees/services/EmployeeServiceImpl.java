package dev.clownsinformatics.tiendajava.rest.employees.services;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.exceptions.EmployeeNotFoundException;
import dev.clownsinformatics.tiendajava.rest.employees.mappers.EmployeeMapper;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import dev.clownsinformatics.tiendajava.rest.employees.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = "employees")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepository;

    @Override
    public Page<EmployeeResponseDto> findAll(Optional<String> name, Optional<Double> minSalary, Optional<Double> maxSalary, Optional<String> position, Pageable pageable) {
        Specification<Employee> specification = getSpecification(name, minSalary, maxSalary, position);
        Page<Employee> employees = employeeRepository.findAll(specification, pageable);
        return employees.map(employeeMapper::toResponseDto);
    }

    @Override
    @CachePut(key = "#id")
    public EmployeeResponseDto findById(Integer id) {
        Employee employee = getEmployee(id);
        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @CachePut(key = "#result.id")
    public EmployeeResponseDto save(CreateEmployeeRequestDto createEmployeeRequestDto) {
        Employee employee = employeeMapper.toEmployee(createEmployeeRequestDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(savedEmployee);
    }

    @Override
    @CachePut(key = "#id")
    public EmployeeResponseDto update(Integer id, UpdateEmployeeRequestDto updateEmployeeRequestDto) {
        Employee dbEmployee = getEmployee(id);
        Employee updatedEmployee = employeeMapper.toEmployee(updateEmployeeRequestDto, dbEmployee);
        Employee dbUpdatedEmployee = employeeRepository.save(updatedEmployee);
        return employeeMapper.toResponseDto(dbUpdatedEmployee);
    }


    @Override
    @CacheEvict(key = "#id")
    public void delete(Integer id) {
        findById(id);
        employeeRepository.deleteById(id);
    }

    private Specification<Employee> getSpecification(Optional<String> name, Optional<Double> minSalary, Optional<Double> maxSalary, Optional<String> position) {
        // Case-insensitive name search
        Specification<Employee> nameSpecification = ((root, query, criteriaBuilder) -> name.map(s -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + s.toLowerCase() + "%")).orElse(criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        // Min and max salary search
        Specification<Employee> minSalarySpecification = ((root, query, criteriaBuilder) -> minSalary.map(s -> criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), s)).orElse(criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Employee> maxSalarySpecification = ((root, query, criteriaBuilder) -> maxSalary.map(s -> criteriaBuilder.lessThanOrEqualTo(root.get("salary"), s)).orElse(criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        // Case-sensitive position search
        Specification<Employee> positionSpecification = ((root, query, criteriaBuilder) -> position.map(s -> criteriaBuilder.equal(root.get("position"), s)).orElse(criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        return Specification.where(nameSpecification).and(minSalarySpecification).and(maxSalarySpecification).and(positionSpecification);
    }

    private Employee getEmployee(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
