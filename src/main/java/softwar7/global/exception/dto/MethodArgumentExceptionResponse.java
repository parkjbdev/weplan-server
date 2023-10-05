package softwar7.global.exception.dto;

import org.springframework.validation.FieldError;

import java.util.Map;

public record MethodArgumentExceptionResponse(
        String statusCode,
        Map<String, String> validation
) {

    public void addValidation(final FieldError fieldError) {
        validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
