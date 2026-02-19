package np.dev.kd.springbootdemoapplication.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeNotFoundException extends RuntimeException {
    private String msg;

    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
