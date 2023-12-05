package softwar7.application.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.domain.schedule.vo.Approval;
import softwar7.global.exception.NotFoundException;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.util.ServiceTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleFindServiceTest extends ServiceTest {

    @Autowired
    private ScheduleFindService scheduleFindService;

    @DisplayName("단일 스케줄 조회")
    @Test
    void getById() {
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
        Schedule findSchedule = scheduleFindService.getById(schedule.getId());
        assertThat(findSchedule).isNotNull();
    }

    @DisplayName("해당 id와 일치하는 스케줄이 존재하지 않으면 예외가 발생한다.")
    @Test
    void getByIdFail() {
        // expected
        assertThatThrownBy(() -> scheduleFindService.getById(9999))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("해당 기간에 승인된 스케줄 목록 조회")
    @Test
    void findAllSchedulesByDate() {
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
                .channelId(channel.getId())
                .startTime(LocalDateTime.of(2023, 12, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2023, 12, 1, 14, 0, 0))
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule);

        // when
        List<ScheduleResponse> scheduleResponses = scheduleFindService.findAllSchedulesByDate(
                LocalDateTime.of(2023, 12, 1, 11, 59, 0),
                LocalDateTime.of(2023, 12, 1, 14, 1, 0),
                channel.getId());

        // then
        assertThat(scheduleResponses.size()).isEqualTo(1);
    }

    @DisplayName("요청받은 스케줄 조회")
    @Test
    void findAllRequestSchedules() {
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
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule3 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        // when
        List<ScheduleResponse> schedules =
                scheduleFindService.findAllRequestSchedules();
        assertThat(schedules.size()).isEqualTo(2);
    }

    @DisplayName("승인 대기 스케줄 조회")
    @Test
    void findAllPendingSchedules() {
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
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule3 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        // when
        List<ScheduleResponse> schedules =
                scheduleFindService.findAllRequestSchedules();
        assertThat(schedules.size()).isEqualTo(2);
    }

    @DisplayName("로그인한 회원의 스케줄 정보를 가져온다.")
    @Test
    void findAllMemberSchedules() {
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
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.PENDING)
                .build();

        Schedule schedule3 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        // when
        List<ScheduleResponse> schedules =
                scheduleFindService.findAllMemberSchedules(null, null, Approval.APPROVED, member.getId());
        assertThat(schedules.size()).isEqualTo(1);
    }
}