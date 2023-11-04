package softwar7.global.exception;

import lombok.Getter;

import static softwar7.global.constant.StatusCodeConstant.NOT_FOUND;

@Getter
public class ForbiddenException extends RuntimeException {

    private final String statusCode = NOT_FOUND.statusCode;
    private final String message;

    public ForbiddenException(final String message) {
        this.message = message;
    }
}
