package softwar7.global.exception;

import lombok.Getter;

import static softwar7.global.constant.StatusCodeConstant.UNAUTHORIZED;

@Getter
public class UnAuthorizedException extends RuntimeException {

    private final String statusCode = UNAUTHORIZED.statusCode;
    private final String message;

    public UnAuthorizedException(final String message) {
        this.message = message;
    }
}
