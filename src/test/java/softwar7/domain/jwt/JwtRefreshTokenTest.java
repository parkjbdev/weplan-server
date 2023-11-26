package softwar7.domain.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtRefreshTokenTest {

    @DisplayName("회원의 토큰 정보를 재발급한다.")
    @Test
    void update() {
        // given
        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
                .memberId(1L)
                .refreshToken("token value")
                .build();

        String updateToken = "update token value";

        // when
        jwtRefreshToken.update(updateToken);

        // then
        assertThat(jwtRefreshToken.getRefreshToken()).isEqualTo(updateToken);
    }
}