package softwar7.mapper;

import softwar7.domain.member.Member;
import softwar7.domain.member.MemberSession;

public enum MemberMapper {

    MemberMapper() {
    };

    public static MemberSession toMemberSession(final Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
