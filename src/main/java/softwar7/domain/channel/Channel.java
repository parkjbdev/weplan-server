package softwar7.domain.channel;

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
public class Channel extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_Id")
    private Long id;

    @Association
    private Long memberId;

    private String channelName;

    private String channelPlace;

    @Builder
    private Channel(final Long memberId, final String channelName, final String channelPlace) {
        this.memberId = memberId;
        this.channelName = channelName;
        this.channelPlace = channelPlace;
    }
}
