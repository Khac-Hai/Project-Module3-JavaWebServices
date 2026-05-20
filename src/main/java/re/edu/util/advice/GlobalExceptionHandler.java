package re.edu.util.advice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re.edu.exception.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.dto.response.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi phương thức", "Phương thức HTTP không được hỗ trợ");

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 405, "Method Not Allowed"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(JwtExceptionCustom.class)
    public ResponseEntity<?> handleJwtException(JwtExceptionCustom e) {
        Map<String, Object> res = new HashMap<>();
        res.put("Lỗi Token", e.getMessage());
        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 401, "Lỗi xác thực Token"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsExceptionCustom.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsExceptionCustom e) {
        Map<String, Object> res = new HashMap<>();
        res.put("Lỗi đăng nhập", e.getMessage());
        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 401, "Lỗi đăng nhập không thành công"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedExceptionCustom.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedExceptionCustom e) {
        Map<String, Object> res = new HashMap<>();
        res.put("Lỗi phân quyền", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 403, "Truy cập bị từ chối"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {

        Map<String, Object> errors = new HashMap<>();
        e.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, errors, 400, "Lấy dữ liệu không thành công"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateResourceException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("Lỗi trùng lặp", e.getMessage());
        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 409, "Lỗi dữ liệu trùng lặp"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        Map<String, Object> res = new HashMap<>();

        String message = e.getMostSpecificCause().getMessage();

        if (message.contains("Boolean")) {
            res.put("Lỗi dữ liệu", "Giá trị truyền vào phải là true hoặc false");
        } else if (message.contains("Role")) {
            res.put("Lỗi dữ liệu", "Role không hợp lệ");
        } else {
            res.put("Lỗi dữ liệu", "Dữ liệu gửi lên không hợp lệ hoặc bị bỏ trống");
        }

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 400, "Dữ liệu không hợp lệ"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi dữ liệu", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 400, "Dữ liệu không hợp lệ"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("Lỗi không tìm thấy", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 404, "Không tìm thấy tài nguyên"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {

        Map<String, Object> res = new HashMap<>();

        String rootMessage =
                ex.getMostSpecificCause().getMessage();

        String message = "Lỗi ràng buộc dữ liệu";

        // ===== DUPLICATE =====
        if (rootMessage.contains("phase_name")
                || rootMessage.contains("phaseName")) {

            message = "Tên giai đoạn đã tồn tại";
        }

        // ===== PRIMARY KEY SEQUENCE =====
        else if (rootMessage.contains("internship_phase_pkey")) {

            message =
                    "Sequence phase_id bị lệch với database";
        }

        // ===== FOREIGN KEY =====
        else if (rootMessage.contains("internship_assignments")) {

            message =
                    "Không thể xóa vì giai đoạn đang được sử dụng";
        }

        // ===== NOT NULL =====
        else if (rootMessage.contains("cannot be null")) {

            message =
                    "Dữ liệu bắt buộc không được để trống";
        }

        res.put("Lỗi dữ liệu", message);

        System.out.println("DB ERROR: " + rootMessage);

        return new ResponseEntity<>(
                MapToAPIResponse.mapTo(
                        null,
                        res,
                        409,
                        "Lỗi ràng buộc dữ liệu"
                ),
                HttpStatus.CONFLICT
        );
    }

}
