package softwar7.domain.schedule.persist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softwar7.domain.BaseTimeEntity;
import softwar7.domain.schedule.vo.Approval;
import softwar7.global.annotation.Association;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Association
    private Long memberId;

    @Association
    private Long channelId;

    private String username;

    private String scheduleName;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Approval approval;

    @Builder
    private Schedule(final long memberId, final long channelId, final String username,
                    final String scheduleName, final String content,
                    final LocalDateTime startTime, final LocalDateTime endTime, final Approval approval) {
        this.memberId = memberId;
        this.channelId = channelId;
        this.username = username;
        this.scheduleName = scheduleName;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.approval = approval;
    }
}
