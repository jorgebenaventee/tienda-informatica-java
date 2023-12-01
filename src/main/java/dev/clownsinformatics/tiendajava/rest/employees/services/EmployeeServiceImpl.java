package dev.clownsinformatics.tiendajava.rest.employees.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketConfig;
import dev.clownsinformatics.tiendajava.config.websocket.WebSocketHandler;
import dev.clownsinformatics.tiendajava.rest.employees.dto.CreateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.EmployeeResponseDto;
import dev.clownsinformatics.tiendajava.rest.employees.dto.UpdateEmployeeRequestDto;
import dev.clownsinformatics.tiendajava.rest.employees.exceptions.EmployeeNotFoundException;
import dev.clownsinformatics.tiendajava.rest.employees.mappers.EmployeeMapper;
import dev.clownsinformatics.tiendajava.rest.employees.models.Employee;
import dev.clownsinformatics.tiendajava.rest.employees.repository.EmployeeRepository;
import dev.clownsinformatics.tiendajava.websockets.notifications.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Implementación de la interfaz {@link EmployeeService} para el manejo de empleados
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "employees")
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final WebSocketConfig webSocketConfig;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private WebSocketHandler webSocketHandler;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, WebSocketConfig webSocketConfig, EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.webSocketConfig = webSocketConfig;
        this.employeeRepository = employeeRepository;
        this.webSocketHandler = webSocketConfig.webSocketEmployeeHandler();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene todos los empleados. Puede filtrar por nombre, salario mínimo, salario máximo y posición
     *
     * @param name      Filtro por nombre
     * @param minSalary Filtro por salario mínimo
     * @param maxSalary Filtro por salario máximo
     * @param position  Filtro por posición
     * @param pageable  Opciones de paginación
     * @return Página de empleados
     */
    @Override
    public Page<EmployeeResponseDto> findAll(Optional<String> name, Optional<Double> minSalary, Optional<Double> maxSalary, Optional<String> position, Pageable pageable) {
        Specification<Employee> specification = getSpecification(name, minSalary, maxSalary, position);
        Page<Employee> employees = employeeRepository.findAll(specification, pageable);
        return employees.map(employeeMapper::toResponseDto);
    }

    /**
     * Obtiene un empleado por su id
     *
     * @param id Id del empleado
     * @return Empleado
     * @throws EmployeeNotFoundException Si no se encuentra el empleado
     */
    @Override
    @CachePut(key = "#id")
    public EmployeeResponseDto findById(Integer id) {
        Employee employee = getEmployee(id);
        return employeeMapper.toResponseDto(employee);
    }

    /**
     * Crea un empleado
     *
     * @param createEmployeeRequestDto Datos del empleado a crear
     * @return Empleado creado
     */
    @Override
    @CachePut(key = "#result.id")
    public EmployeeResponseDto save(CreateEmployeeRequestDto createEmployeeRequestDto) {
        Employee employee = employeeMapper.toEmployee(createEmployeeRequestDto);
        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeResponseDto responseDto = employeeMapper.toResponseDto(savedEmployee);
        sendNotification(Notification.Tipo.CREATE, responseDto);
        return responseDto;
    }

    /**
     * Actualiza un empleado
     *
     * @param id                       Id del empleado a actualizar
     * @param updateEmployeeRequestDto Datos del empleado a actualizar
     * @return Empleado actualizado
     * @throws EmployeeNotFoundException Si no se encuentra el empleado
     */
    @Override
    @CachePut(key = "#id")
    public EmployeeResponseDto update(Integer id, UpdateEmployeeRequestDto updateEmployeeRequestDto) {
        Employee dbEmployee = getEmployee(id);
        Employee updatedEmployee = employeeMapper.toEmployee(updateEmployeeRequestDto, dbEmployee);
        Employee dbUpdatedEmployee = employeeRepository.save(updatedEmployee);
        EmployeeResponseDto responseDto = employeeMapper.toResponseDto(dbUpdatedEmployee);
        sendNotification(Notification.Tipo.UPDATE, responseDto);
        return responseDto;
    }


    /**
     * Elimina un empleado
     *
     * @param id Id del empleado a eliminar
     * @throws EmployeeNotFoundException Si no se encuentra el empleado
     */
    @Override
    @CacheEvict(key = "#id")
    public void delete(Integer id) {
        EmployeeResponseDto employeeResponseDto = findById(id);
        employeeRepository.deleteById(id);
        sendNotification(Notification.Tipo.DELETE, employeeResponseDto);
    }

    private void sendNotification(Notification.Tipo tipo, EmployeeResponseDto data) {
        if (webSocketHandler == null) {
            log.warn("No se ha configurado el servicio de websockets");
            webSocketHandler = this.webSocketConfig.webSocketEmployeeHandler();
        }

        try {
            Notification<EmployeeResponseDto> notification = new Notification<>(
                    "Employee",
                    tipo,
                    data,
                    LocalDate.now().toString()
            );
            String json = objectMapper.writeValueAsString(notification);

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketHandler.sendMessage(json);
                } catch (IOException e) {
                    log.error("Error al enviar la notificacion", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificacion a JSON", e);
        }
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
