package softwar7.mapper.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.mapper.member.dto.MemberResponse;
import softwar7.mapper.member.dto.MemberSignupRequest;

public enum MemberMapper {

    MemberMapper() {
    };

    public static MemberSession toMemberSession(final Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .username(member.getUsername())
                .roleType(member.getRoleType())
                .build();
    }

    public static Member toEntity(final MemberSignupRequest dto, final PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(dto.password());

        return Member.builder()
                .loginId(dto.loginId())
                .password(encodedPassword)
                .username(dto.name())
                .phoneNumber(dto.phoneNumber())
                .roleType(dto.roleType())
                .build();
    }

    public static MemberResponse toMemberResponse(final Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .loginId(member.getLoginId())
                .username(member.getUsername())
                .build();
    }
}
