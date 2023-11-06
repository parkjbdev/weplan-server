package softwar7.mapper.shedule;

import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.domain.schedule.vo.Approval;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;

import java.util.ArrayList;
import java.util.List;

public enum ScheduleMapper {

    ScheduleMapper() {
    };

    public static Schedule toEntity(final MemberSession memberSession, final ScheduleSaveRequest dto) {
        return Schedule.builder()
                .memberId(memberSession.id())
                .channelId(dto.channelId())
                .username(memberSession.username())
                .scheduleName(dto.name())
                .content(dto.content())
                .startTime(dto.start())
                .endTime(dto.end())
                .approval(Approval.PENDING)
                .build();
    }

    public static ScheduleResponse toResponse(final Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getScheduleName())
                .content(schedule.getContent())
                .start(schedule.getStartTime())
                .end(schedule.getEndTime())
                .channelId(schedule.getChannelId())
                .approval(schedule.getApproval())
                .build();
    }

    public static List<ScheduleResponse> toResponses(final List<Schedule> schedules) {
        List<ScheduleResponse> responses = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleResponse response = toResponse(schedule);
            responses.add(response);
        }

        return responses;
    }
}
