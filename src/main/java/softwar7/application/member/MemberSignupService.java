package softwar7.application.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.RoleType;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.member.MemberMapper;
import softwar7.mapper.member.dto.MemberSignupRequest;
import softwar7.repository.member.MemberRepository;

import static softwar7.global.constant.ExceptionMessage.ADMIN_PASSWORD_NOT_MATCH_EXCEPTION;

@Service
public class MemberSignupService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberSignupService(final MemberRepository memberRepository, final PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(final MemberSignupRequest dto) {
        // todo 관리자 권한 리팩토링 필요
        if (dto.roleType() == RoleType.ADMIN && !dto.adminPassword().equals("관리자 비밀번호 1234")) {
            throw new BadRequestException(ADMIN_PASSWORD_NOT_MATCH_EXCEPTION.message);
        }
        Member member = MemberMapper.toEntity(dto, passwordEncoder);
        memberRepository.save(member);
    }
}
