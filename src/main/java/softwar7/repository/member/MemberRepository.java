package softwar7.repository.member;

import org.springframework.stereotype.Repository;
import softwar7.domain.member.persist.Member;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.NotFoundException;

import java.util.Optional;

import static softwar7.global.constant.ExceptionMessage.MEMBER_NOT_FOUND_EXCEPTION;

@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepository(final MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public void save(final Member member) {
        memberJpaRepository.save(member);
    }

    public Member getById(final long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION.message));
    }

    public Member getByLoginId(final String loginId) {
        return memberJpaRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION.message));
    }

    public long count() {
        return memberJpaRepository.count();
    }
}
