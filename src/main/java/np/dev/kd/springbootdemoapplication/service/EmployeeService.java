package np.dev.kd.springbootdemoapplication.service;

import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.exception.EmployeeNotFoundException;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    public Employee addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        employeeRepository.save(employee);
        return employee;
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(EmployeeDTO employeeDTO, Long id) {
        Employee employeeFromDb = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeFromDb.setFirstName(employeeDTO.getFirstName());
        employeeFromDb.setLastName(employeeDTO.getLastName());
        employeeFromDb.setEmail(employeeDTO.getEmail());
        employeeFromDb.setSalary(employeeDTO.getSalary());
        return employeeFromDb;
    }

    public Employee patchEmployee(EmployeeDTO employeeDTO, Long id) {
        Employee employeeFromDb = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeFromDb.setFirstName(employeeDTO.getFirstName() == null || employeeDTO.getFirstName().isEmpty() ? employeeFromDb.getFirstName() : employeeDTO.getFirstName());
        employeeFromDb.setLastName(employeeDTO.getLastName() == null || employeeDTO.getLastName().isEmpty() ? employeeFromDb.getLastName() : employeeDTO.getLastName());
        employeeFromDb.setEmail(employeeDTO.getEmail() == null || employeeDTO.getEmail().isEmpty() ? employeeFromDb.getEmail() : employeeDTO.getEmail());
        employeeFromDb.setSalary(employeeDTO.getSalary() == null ? employeeFromDb.getSalary() : employeeDTO.getSalary());

        return employeeRepository.save(employeeFromDb);
    }

    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
