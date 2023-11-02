package softwar7.mapper.shedule.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScheduleResponse(
        long id,
        String name,
        String content,
        LocalDateTime start,
        LocalDateTime end,
        long channelId
) {
}
