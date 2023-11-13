package softwar7.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import softwar7.domain.member.persist.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(final String loginId);

    Optional<Member> findByPhoneNumber(final String phoneNumber);
}
