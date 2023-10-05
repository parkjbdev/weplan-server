package softwar7.application.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;
import static softwar7.global.constant.TimeConstant.THIRTY_DAY;

@Service
public class SetHeaderService {

    public void setAccessTokenHeader(final HttpServletResponse response, final String accessToken) {
        response.setHeader(ACCESS_TOKEN.value, accessToken);
    }

    public void setRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN.value, refreshToken)
                .maxAge(Duration.ofDays(THIRTY_DAY.value))
                .path("/")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
