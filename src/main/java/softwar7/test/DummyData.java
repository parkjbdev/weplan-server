package softwar7.test;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.RoleType;
import softwar7.repository.member.MemberRepository;

@Service
public class DummyData {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DummyData(final MemberRepository memberRepository, final PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostConstruct
    public void addInitData() {
        String password = "weplan1234";

        Member member = Member.builder()
                .loginId("weplan")
                .password(passwordEncoder.encode(password))
                .username("username")
                .phoneNumber("01012345678")
                .roleType(RoleType.GUEST)
                .build();

        memberRepository.save(member);
    }
}
