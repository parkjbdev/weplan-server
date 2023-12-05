package softwar7.application.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;

class JwtCreateTokenServiceTest extends ServiceTest {

    @Autowired
    protected JwtCreateTokenService jwtCreateTokenService;

    @DisplayName("회원 정보를 가지고 있는 AccessToken을 생성")
    @Test
    void createAccessToken() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1)
                .username("회원 이름")
                .roleType(RoleType.ADMIN)
                .build();

        // when
        String accessToken = jwtCreateTokenService.createAccessToken(memberSession, ONE_HOUR.value);

        // then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("회원 ID를 가지고 있는 RefreshToken을 생성")
    @Test
    void createRefreshToken() {
        // when
        String refreshToken = jwtCreateTokenService.createRefreshToken(1, ONE_HOUR.value);

        // then
        assertThat(refreshToken).isNotNull();
    }
}