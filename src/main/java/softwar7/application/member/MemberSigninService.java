package softwar7.application.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softwar7.application.jwt.JwtFacade;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.global.constant.TimeConstant;
import softwar7.mapper.member.MemberMapper;
import softwar7.mapper.member.dto.MemberSigninRequest;
import softwar7.repository.member.MemberRepository;

import static softwar7.global.constant.TimeConstant.*;


@Service
public class MemberSigninService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtFacade jwtFacade;

    public MemberSigninService(final MemberRepository memberRepository,
                               final PasswordEncoder passwordEncoder, final JwtFacade jwtFacade) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtFacade = jwtFacade;
    }

    public Member signin(final MemberSigninRequest dto, final HttpServletResponse response) {
        Member member = memberRepository.getByLoginId(dto.loginId());
        String encodedPassword = member.getPassword();

        if (passwordEncoder.matches(dto.password(), encodedPassword)) {
            MemberSession memberSession = MemberMapper.toMemberSession(member);
            String accessToken = jwtFacade.createAccessToken(memberSession, ONE_HOUR.value);
            String refreshToken = jwtFacade.createRefreshToken(memberSession.id(), ONE_MONTH.value);
            jwtFacade.saveJwtRefreshToken(memberSession.id(), refreshToken);
            jwtFacade.setHeader(response, accessToken, refreshToken);
        }

        return member;
    }
}
