package softwar7.domain.schedule.persist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import softwar7.domain.schedule.vo.Approval;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ScheduleTest {

    @DisplayName("스케줄 예약 요청을 승인/거절한다.")
    @Test
    void update() {
        // given
        Schedule schedule = Schedule.builder()
                .memberId(1L)
                .channelId(1L)
                .username("회원 이름")
                .scheduleName("스케줄명")
                .content("스케줄 내용")
                .startTime(LocalDateTime.of(2023, 11, 26, 0, 0, 0))
                .endTime(LocalDateTime.of(2023, 11, 26, 1, 0, 0))
                .approval(Approval.PENDING)
                .build();

        // when
        schedule.updateApproval(Approval.APPROVED);

        // then
        assertThat(schedule.getApproval()).isEqualTo(Approval.APPROVED);
    }
}