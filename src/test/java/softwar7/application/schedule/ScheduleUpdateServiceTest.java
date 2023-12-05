package softwar7.application.schedule;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.exception.NotFoundException;
import softwar7.mapper.shedule.dto.ScheduleUpdateRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleUpdateServiceTest extends ServiceTest {

    @Autowired
    protected ScheduleUpdateService scheduleUpdateService;

    @DisplayName("등록된 스케줄 ID를 사용해 형식에 맞는 데이터로 스케줄 업데이트")
    @Test
    void updateSchedule() {
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

        ScheduleUpdateRequest scheduleUpdateRequest = ScheduleUpdateRequest.builder()
                .name("수정 스케줄명")
                .content("수정 스케줄 내용")
                .build();

        // when
        scheduleUpdateService.updateSchedule(schedule.getId(), scheduleUpdateRequest);

        // then
        Schedule findSchedule = scheduleRepository.getById(schedule.getId());
        assertThat(findSchedule.getScheduleName()).isEqualTo("수정 스케줄명");
        assertThat(findSchedule.getContent()).isEqualTo("수정 스케줄 내용");
    }

    @DisplayName("등록되지 않은 스케줄 ID를 사용해 스케줄 업데이트")
    @Test
    void updateScheduleFail() {
        // given
        ScheduleUpdateRequest scheduleUpdateRequest = ScheduleUpdateRequest.builder()
                .name("수정 스케줄명")
                .content("수정 스케줄 내용")
                .build();

        // expected
        assertThatThrownBy(() -> scheduleUpdateService.updateSchedule(9999L, scheduleUpdateRequest))
                .isInstanceOf(NotFoundException.class);
    }
}