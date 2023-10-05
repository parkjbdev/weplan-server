package softwar7.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import softwar7.domain.member.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
