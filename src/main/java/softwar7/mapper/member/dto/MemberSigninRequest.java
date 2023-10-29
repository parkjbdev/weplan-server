package softwar7.mapper.member.dto;

import jakarta.validation.constraints.Size;

public record MemberSigninRequest(
        @Size(min = 6, max = 15, message = "로그인 아이디는 6자 이상 15자 이하를 지원합니다")
        String loginId,
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하를 지원합니다")
        String password
) {
}
