package softwar7.application.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.jwt.JwtRefreshToken;
import softwar7.domain.member.MemberSession;
import softwar7.mapper.jwt.JwtRefreshTokenMapper;
import softwar7.repository.jwt.JwtRefreshTokenRepository;

import java.util.Optional;

@Service
public class JwtFacade {

    private final JwtCreateTokenService jwtCreateTokenService;
    private final SetHeaderService setHeaderService;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public JwtFacade(final JwtCreateTokenService jwtCreateTokenService, final SetHeaderService setHeaderService,
                     final JwtRefreshTokenRepository jwtRefreshTokenRepository) {
        this.jwtCreateTokenService = jwtCreateTokenService;
        this.setHeaderService = setHeaderService;
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
    }

    public String createAccessToken(final MemberSession memberSession, final long expired) {
        return jwtCreateTokenService.createAccessToken(memberSession, expired);
    }

    public String createRefreshToken(final long memberId, final long expired) {
        return jwtCreateTokenService.createRefreshToken(memberId, expired);
    }

    public void setHeader(final HttpServletResponse response, final String accessToken,
                          final String refreshToken) {
        setHeaderService.setAccessTokenHeader(response, accessToken);
        setHeaderService.setRefreshTokenCookie(response, refreshToken);
    }

    @Transactional
    public void saveJwtRefreshToken(final long memberId, final String refreshToken) {
        Optional<JwtRefreshToken> optionalJwtRefreshToken = jwtRefreshTokenRepository.findByMemberId(memberId);
        if (optionalJwtRefreshToken.isPresent()) {
            JwtRefreshToken jwtRefreshToken = optionalJwtRefreshToken.get();
            jwtRefreshToken.update(refreshToken);
        } else {
            JwtRefreshToken entity = JwtRefreshTokenMapper.toEntity(memberId, refreshToken);
            jwtRefreshTokenRepository.save(entity);
        }
    }
}
