package softwar7.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import softwar7.global.exception.dto.ExceptionResponse;

import static org.springframework.web.client.HttpClientErrorException.*;


@RestControllerAdvice
public class ApiRestControllerAdvice {

    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleException(
            final MethodArgumentNotValidException e
    ) {
        return new ExceptionResponse("400", "형식에 맞지 않습니다");
    }

    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ExceptionResponse handleException(
            final MethodArgumentTypeMismatchException e
    ) {
        return new ExceptionResponse("400", "서버에 전달한 인자와 타입이 맞지 않습니다");
    }

    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    public ExceptionResponse handleException(final ServletRequestBindingException e) {
        return new ExceptionResponse("400", "파라미터나 HTTP 바디 데이터를 바인딩 할 수 없습니다");
    }

    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponse handleException(final BadRequestException e) {
        return new ExceptionResponse(e.getStatusCode(), e.getMessage());
    }

    // 401
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public ExceptionResponse handleException(final UnAuthorizedException e) {
        return new ExceptionResponse(e.getStatusCode(), e.getMessage());
    }

    // 403
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ExceptionResponse handleException(final ForbiddenException e) {
        return new ExceptionResponse(e.getStatusCode(), e.getMessage());
    }

    // 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handleException(final NotFoundException e) {
        return new ExceptionResponse(e.getStatusCode(), e.getMessage());
    }

    // 405
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowed.class)
    public ExceptionResponse handleException(final MethodNotAllowed e) {
        return new ExceptionResponse("405", "HTTP 메소드가 올바르지 않습니다");
    }
}
