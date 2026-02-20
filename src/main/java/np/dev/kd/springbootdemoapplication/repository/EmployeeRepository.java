package np.dev.kd.springbootdemoapplication.repository;

import np.dev.kd.springbootdemoapplication.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
}
