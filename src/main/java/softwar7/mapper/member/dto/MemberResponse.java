package softwar7.mapper.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
        long memberId,
        String loginId,
        String username
) {
}
