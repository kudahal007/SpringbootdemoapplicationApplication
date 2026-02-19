package np.dev.kd.springbootdemoapplication.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import np.dev.kd.springbootdemoapplication.validation.ValidationGroups;

public class EmployeeDTO {
    @NotBlank(message = "First name is required", groups = {ValidationGroups.Create.class})
    private String firstName;

    @NotBlank(message = "Last name is required", groups = {ValidationGroups.Create.class})
    private String lastName;

    @NotBlank(message = "Email is required", groups = {ValidationGroups.Create.class})
    @Email(message = "Invalid Email", groups = {ValidationGroups.Create.class, ValidationGroups.Patch.class})
    private String email;

    @NotNull(message = "Salary is required", groups = {ValidationGroups.Create.class})
    @Positive(message = "Salary must be positive", groups = {ValidationGroups.Create.class, ValidationGroups.Patch.class})
    private Double salary;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
