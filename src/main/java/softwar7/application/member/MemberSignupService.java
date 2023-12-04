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

import java.util.Optional;

import static softwar7.global.constant.ExceptionMessage.*;

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
        if (dto.roleType() == RoleType.ADMIN && !dto.adminPassword().equals("관리자 비밀번호 1234")) {
            throw new BadRequestException(ADMIN_PASSWORD_NOT_MATCH_EXCEPTION.message);
        }

        validateDuplication(dto);
        Member member = MemberMapper.toEntity(dto, passwordEncoder);
        memberRepository.save(member);
    }

    private void validateDuplication(final MemberSignupRequest dto) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(dto.loginId());
        Optional<Member> byPhoneNumber = memberRepository.findByPhoneNumber(dto.phoneNumber());

        if (byLoginId.isPresent()) {
            throw new BadRequestException(LOGIN_ID_DUPLICATE_EXCEPTION.message);
        }

        if (byPhoneNumber.isPresent()) {
            throw new BadRequestException(PHONE_NUMBER_DUPLICATE_EXCEPTION.message);
        }
    }
}
