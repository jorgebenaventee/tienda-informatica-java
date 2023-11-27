package dev.clownsinformatics.tiendajava.rest.employees.controllers;

import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.services.EmployeeService;
import dev.clownsinformatics.tiendajava.utils.pagination.PageResponse;
import dev.clownsinformatics.tiendajava.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('USER')")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all employees"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @Parameters({
            @Parameter(name = "name", description = "Employee name"),
            @Parameter(name = "minSalary", description = "Minimum salary"),
            @Parameter(name = "maxSalary", description = "Maximum salary"),
            @Parameter(name = "position", description = "Employee position"),
            @Parameter(name = "page", description = "Page number"),
            @Parameter(name = "size", description = "Page size"),
            @Parameter(name = "sortBy", description = "Sort by"),
            @Parameter(name = "direction", description = "Sort direction")
    })
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

    @Operation(summary = "Get employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found employee"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @Parameters({
            @Parameter(name = "id", description = "Employee id", required = true)
    })
    @GetMapping("{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Integer id) {
        log.info("Getting employee with id: {}", id);
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @Operation(summary = "Create employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created employee"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "employee", description = "Employee to create", required = true)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody CreateEmployeeRequestDto employee) {
        log.info("Creating employee: {}", employee);
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @Operation(summary = "Update employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated employee"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "Employee id", required = true),
            @Parameter(name = "employee", description = "Employee to update", required = true)
    })
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Integer id, @Valid @RequestBody UpdateEmployeeRequestDto employee) {
        log.info("Updating employee with id: {}", id);
        EmployeeResponseDto employeeDto = employeeService.update(id, employee);
        return ResponseEntity.ok(employeeDto);
    }

    @Operation(summary = "Partially update employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partially updated employee"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "Employee id", required = true),
            @Parameter(name = "employeeUpdate", description = "Employee to partially update",required = true)
    })
    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDto> partialUpdateEmployee(@PathVariable Integer id, @Valid @RequestBody UpdateEmployeeRequestDto employee) {
        log.info("Partially updating employee with id: {}", id);
        EmployeeResponseDto employeeDto = employeeService.update(id, employee);
        return ResponseEntity.ok(employeeDto);
    }

    @Operation(summary = "Delete employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted employee"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Parameters({
            @Parameter(name = "id", description = "Employee id", required = true)
    })
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
