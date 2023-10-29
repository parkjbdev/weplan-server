package softwar7.application.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;

@Service
public class SetHeaderService {

    public void setAccessTokenHeader(final HttpServletResponse response, final String accessToken) {
        response.setHeader(ACCESS_TOKEN.value, accessToken);
    }

    public void setRefreshTokenHeader(final HttpServletResponse response, final String refreshToken) {
        response.setHeader(REFRESH_TOKEN.value, refreshToken);
    }
}
