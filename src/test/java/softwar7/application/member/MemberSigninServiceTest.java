package softwar7.application.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.RoleType;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.member.dto.MemberSigninRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;

class MemberSigninServiceTest extends ServiceTest {

    @Autowired
    private MemberSigninService memberSigninService;

    @DisplayName("등록된 ID 및 패스워드로 로그인")
    @Test
    void signin() {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .roleType(RoleType.GUEST)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSigninRequest memberSigninRequest = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .build();

        // given 3
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        memberSigninService.signin(memberSigninRequest, response);

        // then
        assertThat(response.getHeader(ACCESS_TOKEN.value)).isNotNull();
        assertThat(response.getHeader(REFRESH_TOKEN.value)).isNotNull();
    }

    @DisplayName("등록되지 않은 ID 및 패스워드로 로그인")
    @Test
    void signinFail() {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .roleType(RoleType.GUEST)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSigninRequest memberSigninRequest = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("일치하지 않은 정보")
                .build();

        // given 3
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        assertThatThrownBy(() -> memberSigninService.signin(memberSigninRequest, response))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("로그인시 관리자 권한이면 true를 반환한다.")
    @Test
    void signinCheckAdminRoleType() {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSigninRequest memberSigninRequest = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .build();

        // given 3
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        Boolean isAdminRoleType = memberSigninService.signin(memberSigninRequest, response);

        // then
        assertThat(isAdminRoleType).isTrue();
    }

    @DisplayName("로그인시 게스트 권한이면 false를 반환한다.")
    @Test
    void signinCheckGuestRoleType() {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .roleType(RoleType.GUEST)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSigninRequest memberSigninRequest = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .build();

        // given 3
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        Boolean isAdminRoleType = memberSigninService.signin(memberSigninRequest, response);

        // then
        assertThat(isAdminRoleType).isFalse();
    }
}