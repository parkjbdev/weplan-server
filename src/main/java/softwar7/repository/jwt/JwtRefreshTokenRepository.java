package softwar7.repository.jwt;

import org.springframework.stereotype.Repository;
import softwar7.domain.jwt.JwtRefreshToken;
import softwar7.global.exception.NotFoundException;

import java.util.Optional;

import static softwar7.global.constant.ExceptionMessage.REFRESH_TOKEN_NOT_FOUND_EXCEPTION;

@Repository
public class JwtRefreshTokenRepository {

    private final JwtRefreshTokenJpaRepository jwtRefreshTokenJpaRepository;

    public JwtRefreshTokenRepository(final JwtRefreshTokenJpaRepository jwtRefreshTokenJpaRepository) {
        this.jwtRefreshTokenJpaRepository = jwtRefreshTokenJpaRepository;
    }

    public void save(final JwtRefreshToken jwtRefreshToken) {
        jwtRefreshTokenJpaRepository.save(jwtRefreshToken);
    }

    public JwtRefreshToken getByMemberId(final long memberId) {
        return jwtRefreshTokenJpaRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION.message));
    }

    public Optional<JwtRefreshToken> findByMemberId(final long memberId) {
        return jwtRefreshTokenJpaRepository.findByMemberId(memberId);
    }
}
