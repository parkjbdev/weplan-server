package softwar7.application.schedule;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.exception.ForbiddenException;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleDeleteServiceTest extends ServiceTest {

    @Autowired
    protected ScheduleDeleteService scheduleDeleteService;

    @DisplayName("스케줄 삭제")
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
        scheduleDeleteService.deleteSchedule(member.getId(), schedule.getId());

        // then
        assertThat(scheduleRepository.count()).isEqualTo(0);
    }

    @DisplayName("권한 없는 사용자가 스케줄 삭제")
    @Test
    void deleteScheduleFail() {
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

        // expected
        assertThatThrownBy(() -> scheduleDeleteService.deleteSchedule(9999L, schedule.getId()))
                .isInstanceOf(ForbiddenException.class);
    }
}