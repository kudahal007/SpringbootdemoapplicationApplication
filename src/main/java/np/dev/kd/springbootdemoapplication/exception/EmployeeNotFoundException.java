package np.dev.kd.springbootdemoapplication.exception;

public class EmployeeNotFoundException extends RuntimeException{
    private String msg;
    public EmployeeNotFoundException(String msg){
       super(msg);
    }
}
