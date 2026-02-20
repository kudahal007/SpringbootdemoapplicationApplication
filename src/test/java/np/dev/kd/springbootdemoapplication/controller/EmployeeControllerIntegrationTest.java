package np.dev.kd.springbootdemoapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.repository.EmployeeRepository;
import np.dev.kd.springbootdemoapplication.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class EmployeeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repository;
    Employee setupEmployee;

    @BeforeEach
    void setup() {
        setupEmployee = repository.save(new Employee("John", "Doe", "johndoe@example.com", 150000));
    }

    @Test
    @DisplayName("Create employee in db")
    void shouldCreateEmpolyeeInDb() throws Exception {
//        Given
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("test@example.com");
        dto.setSalary(100000D);

        long beforeCount = repository.count();
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Get employee by id integration test")
    void shouldGetEmployeeById() throws Exception {

        mockMvc.perform(get("/employees/" + setupEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(setupEmployee.getId()));
    }

    @Test
    @DisplayName("Get all employees integration test")
    void shouldGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("Update existing employee in db")
    void shouldUpdateEmployee() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("Jack");
        dto.setLastName("Doe");
        dto.setEmail("jackdoe@example.com");
        dto.setSalary(100000D);
        mockMvc.perform(put("/employees/" + setupEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(setupEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value("Jack"));
    }

    @Test
    @DisplayName("Partial update existing employee in db integration test")
    void shouldUpdateEmployeePartially() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmail("jack@example.com");
        dto.setSalary(150000D);
        mockMvc.perform(patch("/employees/" + setupEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(setupEmployee.getId()))
                .andExpect(jsonPath("$.email").value("jack@example.com"))
                .andExpect(jsonPath("$.salary").value(150000));
    }

    @Test
    @DisplayName("Delete existing employee in db integration test")
    void shouldDeleteEmplyeeById() throws Exception {
        mockMvc.perform(delete("/employees/" + setupEmployee.getId()))
                .andExpect(status().isNoContent());
    }
}
