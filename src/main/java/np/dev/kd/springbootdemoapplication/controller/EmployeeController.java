package np.dev.kd.springbootdemoapplication.controller;

import np.dev.kd.springbootdemoapplication.dto.EmployeeDTO;
import np.dev.kd.springbootdemoapplication.model.Employee;
import np.dev.kd.springbootdemoapplication.service.EmployeeService;
import np.dev.kd.springbootdemoapplication.validation.ValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Validated(ValidationGroups.Create.class) @RequestBody EmployeeDTO employee) {

        Employee savedEmployee = employeeService.addEmployee(employee);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmployee.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(Pageable pageable){
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@Validated(ValidationGroups.Create.class) @RequestBody EmployeeDTO employeeDTO,
                                                   @PathVariable Long id){
        return ResponseEntity.ok(employeeService.updateEmployee(employeeDTO,id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> patchEmployee(@Validated(ValidationGroups.Patch.class) @RequestBody EmployeeDTO employeeDTO,
                                                   @PathVariable Long id){
        return ResponseEntity.ok(employeeService.patchEmployee(employeeDTO,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
