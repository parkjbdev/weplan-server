package softwar7.application.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.RoleType;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.member.dto.MemberSignupRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberSignupServiceTest extends ServiceTest {

    @Autowired
    protected MemberSignupService memberSignupService;

    @DisplayName("게스트 권한으로 입력한 정보를 회원 정보를 DB에 저장한다.")
    @Test
    void signupGuest() {
        // given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호1234")
                .name("회원 이름")
                .phoneNumber("010-1234-5678")
                .build();

        // when
        memberSignupService.signup(memberSignupRequest);

        // then
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    @DisplayName("관리자 권한으로 입력한 정보를 회원 정보를 DB에 저장한다.")
    @Test
    void signupAdmin() {
        // given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .name("회원 이름")
                .phoneNumber("010-1234-5678")
                .roleType(RoleType.ADMIN)
                .adminPassword("관리자 비밀번호 1234")
                .build();

        // when
        memberSignupService.signup(memberSignupRequest);

        // then
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    @DisplayName("관리자 비밀번호가 일치하지 않으면 회원가입시 예외가 발생한다.")
    @Test
    void signupAdminFail() {
        // given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .name("회원 이름")
                .phoneNumber("010-1234-5678")
                .roleType(RoleType.ADMIN)
                .adminPassword("관리자 비밀번호 불일치")
                .build();

        // expected
        assertThatThrownBy(() -> memberSignupService.signup(memberSignupRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("이미 가입된 회원이 있으면 예외가 발생한다.")
    @Test
    void validateDuplication() {
        // given 1
        Member member = Member.builder()
                .loginId("로그인 아이디")
                .build();

        memberRepository.save(member);

        // given 2
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .name("회원 이름")
                .phoneNumber("010-1234-5678")
                .build();

        // expected
        assertThatThrownBy(() -> memberSignupService.signup(memberSignupRequest))
                .isInstanceOf(BadRequestException.class);
    }
}