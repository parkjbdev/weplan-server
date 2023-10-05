package softwar7.domain.jwt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softwar7.domain.BaseTimeEntity;
import softwar7.global.annotation.Association;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class JwtRefreshToken extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_refresh_token_id")
    private Long id;

    @Association
    private Long memberId;

    private String refreshToken;

    @Builder
    private JwtRefreshToken(final Long memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public void update(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}