package np.dev.kd.springbootdemoapplication.service;


import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.exception.EmployeeNotFoundException;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class EmployeeServiceTest {
    @Mock
    private EmployeeRepository repository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private EmployeeService service;

    private EmployeeDTO dto;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("johndoe@example.com");
        dto.setSalary(150000D);

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());

    }

    @Test
    @DisplayName("Should save employee successfully")
    void shouldAddEmployee() {
//        Given
        given(mapper.map(dto, Employee.class)).willReturn(employee);
        given(repository.save(any())).willReturn(employee);

//        when
        Employee saved = service.addEmployee(dto);

//        then
        then(saved).isNotNull();
        then(saved.getFirstName()).isEqualTo("John");
        then(saved.getId()).isEqualTo(1);
        BDDMockito.then(repository).should().save(employee);
    }

    @Test
    @DisplayName("Should get employee by id")
    void shouldGetEmployee() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.of(employee));

        // When
        Employee found = service.getEmployee(1L);

        // Then
        then(found).isNotNull();
        then(found.getEmail()).isEqualTo("johndoe@example.com");
        BDDMockito.then(repository).should().findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when employee not found")
    void shouldThrowWhenEmployeeNotFound() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EmployeeNotFoundException.class,
                () -> service.getEmployee(1L));
    }

    @Test
    @DisplayName("Should return all employees pageable")
    void shouldReturnAllEmployees() {
        // Given
        Page<Employee> page = new PageImpl<>(List.of(employee));
        given(repository.findAll(any(Pageable.class))).willReturn(page);

        // When
        Page<Employee> result = service.getAllEmployees(Pageable.unpaged());

        // Then
        then(result.getContent()).hasSize(1);
        then(result.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should update employee successfully")
    void shouldUpdateEmployee() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.of(employee));

        EmployeeDTO updateDto = new EmployeeDTO();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setEmail("jane@example.com");
        updateDto.setSalary(120000D);

        // When
        Employee updated = service.updateEmployee(updateDto, 1L);

        // Then
        then(updated.getFirstName()).isEqualTo("Jane");
        then(updated.getEmail()).isEqualTo("jane@example.com");

    }
    @Test
    @DisplayName("Should throw exception when updating non-existent employee")
    void shouldThrowWhenUpdateEmployeeNotFound(){
        given(repository.findById(1L)).willReturn(Optional.empty());

        EmployeeDTO updateDto = new EmployeeDTO();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setEmail("jane@example.com");
        updateDto.setSalary(120000D);
        assertThrows(EmployeeNotFoundException.class,()->service.updateEmployee(updateDto,1L));
    }

    @Test
    @DisplayName("Should patch employee successfully")
    void shouldPatchEmployee() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.of(employee));
        given(repository.save(employee)).willReturn(employee);

        EmployeeDTO patchDto = new EmployeeDTO();
        patchDto.setFirstName("Johnny"); // Only patch firstName

        // When
        Employee patched = service.patchEmployee(patchDto, 1L);

        // Then
        then(patched.getFirstName()).isEqualTo("Johnny");
        then(patched.getLastName()).isEqualTo("Doe"); // unchanged
        BDDMockito.then(repository).should().save(employee);
    }
    @Test
    @DisplayName("Should throw exception when patching non-existent employee")
    void shouldThrowWhenPatchEmployeeNotFound(){
        given(repository.findById(1L)).willReturn(Optional.empty());

        EmployeeDTO updateDto = new EmployeeDTO();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setEmail("jane@example.com");
        updateDto.setSalary(120000D);
        assertThrows(EmployeeNotFoundException.class,()->service.patchEmployee(updateDto,1L));
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void shouldDeleteEmployee() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.of(employee));

        // When
        service.deleteEmployee(1L);

        // Then
        BDDMockito.then(repository).should().deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception on delete if employee not found")
    void shouldThrowOnDeleteWhenNotFound() {
        // Given
        given(repository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EmployeeNotFoundException.class,
                () -> service.deleteEmployee(1L));
    }
}