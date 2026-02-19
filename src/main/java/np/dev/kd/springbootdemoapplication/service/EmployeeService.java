package np.dev.kd.springbootdemoapplication.service;

import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.exception.EmployeeNotFoundException;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    public Employee addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        employeeRepository.save(employee);
        return employee;
    }

    @Cacheable(value = "employees",key = "#id")
    public Employee getEmployee(Long id) {
        Employee response= employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with id: " + id));
        log.info("Employee with id: {} retrieved",id);
        return response;


    }

    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
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
