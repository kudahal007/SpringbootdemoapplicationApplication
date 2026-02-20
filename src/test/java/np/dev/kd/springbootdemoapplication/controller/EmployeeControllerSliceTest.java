package np.dev.kd.springbootdemoapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmployeeController.class)
class EmployeeControllerSliceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("Given valid employee dto data when POST /employees then return 201 Created with Location header")
    void shouldCreateEmployee() throws Exception {
//        Given
        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setFirstName("John");
        savedEmployee.setLastName("Doe");
        savedEmployee.setEmail("johndoe@example.com");
        savedEmployee.setSalary(150000);

        given(employeeService.addEmployee(any()))
                .willReturn(savedEmployee);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("johndoe@example.com");
        employeeDTO.setSalary(150000D);

        mvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("Given employee exists when GET /employees/{id} then return 200OK")
    void shouldReturnEmployeeById() throws Exception {
//            Given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("johndoe@example.com");
        employee.setSalary(150000);

        given(employeeService.getEmployee(1L))
                .willReturn(employee);

//        when and then
        mvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

    }

    @Test
    @DisplayName("When GET /employees with pagination then return paged result")
    void shouldReturnPagedEmployees() throws Exception {

        // Given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");

        given(employeeService.getAllEmployees(any()))
                .willReturn(new PageImpl<>(List.of(employee)));

        // When & Then
        mvc.perform(get("/employees?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("John"));
    }

    @Test
    @DisplayName("Given valid data when PUT /employees/{id} then return updated employee")
    void shouldUpdateEmployee() throws Exception {

        // Given
        Employee updated = new Employee();
        updated.setId(1L);
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setEmail("johndoe@example.com");
        updated.setSalary(150000);

        given(employeeService.updateEmployee(any(), eq(1L)))
                .willReturn(updated);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("johndoe@example.com");
        dto.setSalary(150000D);

        // When & Then
        mvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("Given partial data when PATCH /employees/{id} then return patched employee")
    void shouldPatchEmployee() throws Exception {

        // Given
        Employee patched = new Employee();
        patched.setId(1L);
        patched.setFirstName("John");
        patched.setLastName("Doe");

        given(employeeService.patchEmployee(any(), eq(1L)))
                .willReturn(patched);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("Patched");

        // When & Then
        mvc.perform(patch("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("When DELETE /employees/{id} then return 204 No Content")
    void shouldDeleteEmployee() throws Exception {
        mvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
        then(employeeService).should().deleteEmployee(1L);
    }
}
