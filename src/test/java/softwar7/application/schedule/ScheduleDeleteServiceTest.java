package softwar7.application.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleDeleteServiceTest extends ServiceTest {

    @Autowired
    protected ScheduleDeleteService scheduleDeleteService;

    @DisplayName("스케줄 id로 해당 스케줄을 삭제한다.")
    @Test
    void deleteSchedule() {
        // given 1
        Member member = Member.builder()
                .loginId("로그인 아이디")
                .build();

        memberRepository.save(member);

        // given 2
        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널 이름")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .build();

        scheduleRepository.save(schedule);

        // when
        scheduleDeleteService.deleteSchedule(schedule.getId());

        // then
        assertThat(scheduleRepository.count()).isEqualTo(0);
    }
}