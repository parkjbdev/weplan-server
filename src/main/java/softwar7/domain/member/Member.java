package softwar7.domain.member;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softwar7.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String nickname;

    private String loginId;

    private String password;

    @Builder
    private Member(final String username, final String nickname,
                   final String loginId, final String password) {
        this.username = username;
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
    }
}
