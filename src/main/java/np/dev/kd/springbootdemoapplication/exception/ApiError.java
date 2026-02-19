package np.dev.kd.springbootdemoapplication.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(int status, String error, Map<String,String> message, String path, LocalDateTime timestamp) {
}
