package softwar7.domain.member.persist;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softwar7.domain.BaseTimeEntity;
import softwar7.domain.member.vo.RoleType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String password;

    private String username;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    private Member(final String loginId, final String password, final String username,
                  final String phoneNumber, final RoleType roleType) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.roleType = roleType;
    }
}
