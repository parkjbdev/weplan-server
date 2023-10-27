package softwar7.mapper.member.dto;

import softwar7.domain.member.vo.RoleType;

public record MemberSignupRequest(
        String loginId,
        String password,
        String name,
        String phoneNumber,
        RoleType roleType,
        String adminPassword
) {
}
