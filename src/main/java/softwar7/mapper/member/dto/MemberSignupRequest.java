package softwar7.mapper.member.dto;

import jakarta.validation.constraints.Size;
import softwar7.domain.member.vo.RoleType;

public record MemberSignupRequest(
        @Size(min = 6, max = 15, message = "로그인 아이디는 6자 이상 15자 이하를 지원합니다")
        String loginId,
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하를 지원합니다")
        String password,
        @Size(min = 1, max = 10, message = "이름은 1자 이상 10자 이하를 지원합니다")
        String name,
        @Size(max = 13, message = "전화번호를 잘못 입력했습니다")
        String phoneNumber,
        RoleType roleType,
        @Size(min = 8, max = 15, message = "관리자 비밀번호는 8자 이상 15자 이하를 지원합니다")
        String adminPassword
) {
}
