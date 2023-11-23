package softwar7.mapper.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import softwar7.domain.member.vo.RoleType;

import java.util.Arrays;

@Builder
public record MemberSignupRequest(
        @Size(min = 6, max = 15, message = "로그인 아이디는 6자 이상 15자 이하를 지원합니다")
        String loginId,
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하를 지원합니다")
        String password,
        @Size(min = 1, max = 10, message = "이름은 1자 이상 10자 이하를 지원합니다")
        String name,
        @Size(max = 11, message = "전화번호를 잘못 입력했습니다")
        String phoneNumber,
        @NotNull(message = "권한을 입력해주세요")
        RoleType roleType,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String adminPassword
) {
        @AssertTrue(message = "잘못된 권한 값입니다")
        private boolean isRoleTypeValid() {
                return roleType != null && Arrays.asList(RoleType.values()).contains(roleType);
        }

}
