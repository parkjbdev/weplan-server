package softwar7.global.exception;

import lombok.Getter;

import static softwar7.global.constant.StatusCodeConstant.BAD_REQUEST;

@Getter
public class BadRequestException extends RuntimeException {

    private final String statusCode = BAD_REQUEST.statusCode;
    private final String message;

    public BadRequestException(final String message) {
        this.message = message;
    }
}