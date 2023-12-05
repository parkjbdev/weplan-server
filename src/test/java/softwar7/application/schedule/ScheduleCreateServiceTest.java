package softwar7.application.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;
import softwar7.util.ServiceTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleCreateServiceTest extends ServiceTest {

    @Autowired
    private ScheduleCreateService scheduleCreateService;

    @DisplayName("형식에 맞는 데이터로 DateTime이 겹치지 않는 스케줄 생성")
    @Test
    void create() {
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
        MemberSession memberSession = MemberSession.builder()
                .id(1)
                .username("회원 이름")
                .roleType(RoleType.ADMIN)
                .build();

        ScheduleSaveRequest scheduleSaveRequest = ScheduleSaveRequest.builder()
                .name("스케줄명")
                .content("스케줄 내용")
                .start(LocalDateTime.of(2023, 12, 1, 0, 0, 0))
                .end(LocalDateTime.of(2023, 12, 2, 0, 0, 0))
                .channelId(channel.getId())
                .build();

        // when
        scheduleCreateService.create(memberSession, scheduleSaveRequest);

        // then
        assertThat(scheduleRepository.count()).isEqualTo(1);
    }

    @DisplayName("형식에 맞는 데이터로 DateTime이 겹치는 스케줄 생성")
    @Test
    void createFail() {
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
                .startTime(LocalDateTime.of(2023, 12, 1, 0, 0, 0))
                .endTime(LocalDateTime.of(2023, 12, 2, 0, 0, 0))
                .build();

        scheduleRepository.save(schedule);

        MemberSession memberSession = MemberSession.builder()
                .id(1)
                .username("회원 이름")
                .roleType(RoleType.ADMIN)
                .build();

        ScheduleSaveRequest scheduleSaveRequest = ScheduleSaveRequest.builder()
                .name("스케줄명")
                .content("스케줄 내용")
                .start(LocalDateTime.of(2023, 12, 1, 0, 0, 0))
                .end(LocalDateTime.of(2023, 12, 2, 0, 0, 0))
                .channelId(channel.getId())
                .build();

        // expected
        assertThatThrownBy(() -> scheduleCreateService.create(memberSession, scheduleSaveRequest))
                .isInstanceOf(BadRequestException.class);
    }
}