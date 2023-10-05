package softwar7.repository.member;

import org.springframework.stereotype.Repository;
import softwar7.domain.member.Member;
import softwar7.global.exception.NotFoundException;

import static softwar7.global.constant.ExceptionMessage.MEMBER_NOT_FOUND_EXCEPTION;

@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepository(final MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public Member save(final Member member) {
        return memberJpaRepository.save(member);
    }

    public Member getById(final long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION.message));
    }

    public long count() {
        return memberJpaRepository.count();
    }
}
