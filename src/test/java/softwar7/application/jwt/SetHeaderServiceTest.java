package softwar7.application.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;

class SetHeaderServiceTest extends ServiceTest {

    @Autowired
    private SetHeaderService setHeaderService;

    @DisplayName("HTTP 헤더 필드 AccessToken에 토큰 삽입")
    @Test
    void setAccessTokenHeader() {
        // given
        String testAccessToken = "Test AccessToken";
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        setHeaderService.setAccessTokenHeader(response, testAccessToken);
        String accessToken = response.getHeader(ACCESS_TOKEN.value);

        // then
        assertThat(accessToken).isEqualTo("Test AccessToken");
    }

    @DisplayName("HTTP 헤더 필드 RefreshToken에 토큰 삽입")
    @Test
    void setRefreshTokenHeader() {
        // given
        String testRefreshToken = "Test RefreshToken";
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        setHeaderService.setRefreshTokenHeader(response, testRefreshToken);
        String refreshToken = response.getHeader(REFRESH_TOKEN.value);

        // then
        assertThat(refreshToken).isEqualTo("Test RefreshToken");
    }
}