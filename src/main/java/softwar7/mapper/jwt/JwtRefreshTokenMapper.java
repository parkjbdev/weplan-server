package softwar7.mapper.jwt;

import softwar7.domain.jwt.JwtRefreshToken;

public enum JwtRefreshTokenMapper {

    JwtRefreshTokenMapper() {
    };

    public static JwtRefreshToken toEntity(final long memberId, final String refreshToken) {
        return JwtRefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .build();
    }
}
