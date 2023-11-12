package dev.clownsinformatics.tiendajava.rest.employees.controllers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.services.EmployeeService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Double> minSalary,
            @RequestParam(required = false) Optional<Double> maxSalary,
            @RequestParam(required = false) Optional<String> position,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Getting all employees with name: {}, minSalary: {}, maxSalary: {}, position: {}", name, minSalary, maxSalary, position);
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<EmployeeResponseDto> employees = employeeService.findAll(name, minSalary, maxSalary, position, PageRequest.of(page, size, sort));
        PageResponse<EmployeeResponseDto> response = PageResponse.of(employees, sortBy, direction);
        String linkHeader = paginationLinksUtils.createLinkHeader(employees, uriBuilder);

        return ResponseEntity.ok()
                .header("link", linkHeader)
                .body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Integer id) {
        log.info("Getting employee with id: {}", id);
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody CreateEmployeeRequestDto employee) {
        log.info("Creating employee: {}", employee);
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Integer id, @Valid @RequestBody UpdateEmployeeRequestDto employee) {
        log.info("Updating employee with id: {}", id);
        EmployeeResponseDto employeeDto = employeeService.update(id, employee);
        return ResponseEntity.ok(employeeDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<EmployeeResponseDto> partialUpdateEmployee(@PathVariable Integer id, @Valid @RequestBody UpdateEmployeeRequestDto employee) {
        log.info("Partially updating employee with id: {}", id);
        EmployeeResponseDto employeeDto = employeeService.update(id, employee);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        log.info("Deleting employee with id: {}", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
