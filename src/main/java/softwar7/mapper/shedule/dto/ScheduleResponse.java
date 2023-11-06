package softwar7.mapper.shedule.dto;

import lombok.Builder;
import softwar7.domain.schedule.vo.Approval;

import java.time.LocalDateTime;

@Builder
public record ScheduleResponse(
        long scheduleId,
        String name,
        String content,
        LocalDateTime start,
        LocalDateTime end,
        long channelId,
        Approval approval
) {
}
