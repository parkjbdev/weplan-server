package softwar7.application.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import softwar7.domain.jwt.JwtRefreshToken;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;

class JwtManagerTest extends ServiceTest {

    @Autowired
    private JwtManager jwtManager;

    @DisplayName("회원 정보를 가지고 있는 AccessToken을 발급")
    @Test
    void createAccessToken() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1)
                .username("회원 이름")
                .roleType(RoleType.ADMIN)
                .build();

        // when
        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        // then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("회원 ID를 가지고 있는 RefreshToken을 발급")
    @Test
    void createRefreshToken() {
        // when
        String refreshToken = jwtManager.createRefreshToken(1, ONE_HOUR.value);

        // then
        assertThat(refreshToken).isNotNull();
    }

    @DisplayName("HTTP 헤더 필드에 로그인 토큰 정보 삽입")
    @Test
    void setAccessTokenHeader() {
        // given
        String testAccessToken = "Test AccessToken";
        String testRefreshToken = "Test RefreshToken";
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        jwtManager.setHeader(response, testAccessToken, testRefreshToken);
        String accessToken = response.getHeader(ACCESS_TOKEN.value);
        String refreshToken = response.getHeader(REFRESH_TOKEN.value);

        // then
        assertThat(accessToken).isEqualTo("Test AccessToken");
        assertThat(refreshToken).isEqualTo("Test RefreshToken");
    }

    @DisplayName("로그인한 회원의 토큰 관련 정보를 DB에 저장")
    @Test
    void saveJwtRefreshToken() {
        // given
        Member member = Member.builder()
                .loginId("로그인 아이디")
                .build();

        memberRepository.save(member);

        String testRefreshToken = "Test RefreshToken";

        // when
        jwtManager.saveJwtRefreshToken(member.getId(), testRefreshToken);

        // then
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.getByMemberId(member.getId());
        assertThat(jwtRefreshTokenRepository.count()).isEqualTo(1);
        assertThat(jwtRefreshToken.getRefreshToken()).isEqualTo("Test RefreshToken");
    }

    @DisplayName("이미 존재하는 회원의 토큰 관련 정보 DB에서 업데이트")
    @Test
    void updateJwtRefreshToken() {
        // given
        Member member = Member.builder()
                .loginId("로그인 아이디")
                .build();

        memberRepository.save(member);

        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
                .memberId(member.getId())
                .refreshToken("Test RefreshToken")
                .build();

        jwtRefreshTokenRepository.save(jwtRefreshToken);

        // when
        jwtManager.saveJwtRefreshToken(member.getId(), "update RefreshToken");

        // then
        JwtRefreshToken byMemberId = jwtRefreshTokenRepository.getByMemberId(member.getId());
        assertThat(jwtRefreshTokenRepository.count()).isEqualTo(1);
        assertThat(byMemberId.getRefreshToken()).isEqualTo("update RefreshToken");
    }
}