package softwar7.repository.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import softwar7.domain.jwt.JwtRefreshToken;

import java.util.Optional;

public interface JwtRefreshTokenJpaRepository extends JpaRepository<JwtRefreshToken, Long> {

    Optional<JwtRefreshToken> findByMemberId(final long memberId);
}
