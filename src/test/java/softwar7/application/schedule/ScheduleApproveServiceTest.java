package softwar7.application.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.mapper.shedule.dto.ScheduleApproveRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static softwar7.domain.schedule.vo.Approval.*;

class ScheduleApproveServiceTest extends ServiceTest {

    @Autowired
    protected ScheduleApproveService scheduleApproveService;

    @DisplayName("관리자가 신청한 예약 스케줄을 승인한다.")
    @Test
    void approveSchedule() {
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
                .approval(PENDING)
                .build();

        scheduleRepository.save(schedule);
        ScheduleApproveRequest scheduleApproveRequest = new ScheduleApproveRequest(schedule.getId(), APPROVED);

        // when
        scheduleApproveService.approveSchedule(scheduleApproveRequest);

        // then
        Schedule findSchedule = scheduleRepository.getById(schedule.getId());
        assertThat(findSchedule.getApproval()).isEqualTo(APPROVED);
    }

    @DisplayName("관리자가 신청한 예약 스케줄을 거절한다.")
    @Test
    void rejectSchedule() {
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
                .approval(PENDING)
                .build();

        scheduleRepository.save(schedule);
        ScheduleApproveRequest scheduleApproveRequest = new ScheduleApproveRequest(schedule.getId(), REJECTED);

        // when
        scheduleApproveService.approveSchedule(scheduleApproveRequest);

        // then
        Schedule findSchedule = scheduleRepository.getById(schedule.getId());
        assertThat(findSchedule.getApproval()).isEqualTo(REJECTED);
    }
}