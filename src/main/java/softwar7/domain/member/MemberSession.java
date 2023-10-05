package softwar7.domain.member;

import lombok.Builder;

@Builder
public record MemberSession(
        long id,
        String username,
        String nickname
) {
}
