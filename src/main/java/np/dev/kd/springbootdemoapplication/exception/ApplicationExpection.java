package np.dev.kd.springbootdemoapplication.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class ApplicationExpection {
    private static final Logger log = LoggerFactory.getLogger(ApplicationExpection.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err->fieldErrors.put(err.getField(),err.getDefaultMessage()));

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(),
                "Data Validation Error",
                fieldErrors,
                request.getRequestURI(),
                LocalDateTime.now());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDatabaseException(DataAccessException ex,
                                                            HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(),
                "User data error",
                Map.of("message","Duplicate data"),
                request.getRequestURI(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleEmployeeNotFoundException(EmployeeNotFoundException ex,
                                                                HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(),
                "Not Found",
                Map.of("message",ex.getLocalizedMessage()),
                request.getRequestURI(),
                LocalDateTime.now());
        log.error(error.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
