package softwar7.mapper.shedule.dto;


import java.time.LocalDateTime;

public record ScheduleSaveRequest(
        String name,
        String content,
        LocalDateTime start,
        LocalDateTime end,
        long channelId
) {
}
