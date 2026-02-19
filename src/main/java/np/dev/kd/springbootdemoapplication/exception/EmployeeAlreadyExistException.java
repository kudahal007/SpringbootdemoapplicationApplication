package np.dev.kd.springbootdemoapplication.exception;

public class EmployeeAlreadyExistException extends RuntimeException{
    private String msg;
    public EmployeeAlreadyExistException(String msg){
        super(msg);
    }
}
