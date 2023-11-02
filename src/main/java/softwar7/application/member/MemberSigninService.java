package softwar7.application.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softwar7.application.jwt.JwtManager;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.member.MemberMapper;
import softwar7.mapper.member.dto.MemberSigninRequest;
import softwar7.repository.member.MemberRepository;

import static softwar7.global.constant.ExceptionMessage.*;
import static softwar7.global.constant.TimeConstant.*;


@Service
public class MemberSigninService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;

    public MemberSigninService(final MemberRepository memberRepository,
                               final PasswordEncoder passwordEncoder, final JwtManager jwtManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtManager = jwtManager;
    }

    public Boolean signin(final MemberSigninRequest dto, final HttpServletResponse response) {
        Member member = memberRepository.getByLoginId(dto.loginId());
        String encodedPassword = member.getPassword();

        if (passwordEncoder.matches(dto.password(), encodedPassword)) {
            MemberSession memberSession = MemberMapper.toMemberSession(member);
            String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);
            String refreshToken = jwtManager.createRefreshToken(memberSession.id(), ONE_MONTH.value);
            jwtManager.saveJwtRefreshToken(memberSession.id(), refreshToken);
            jwtManager.setHeader(response, accessToken, refreshToken);

            return member.getRoleType().equals(RoleType.ADMIN);
        }

        throw new BadRequestException(PASSWORD_NOT_MATCH_EXCEPTION.message);
    }
}
