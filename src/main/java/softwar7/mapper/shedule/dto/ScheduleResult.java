package softwar7.mapper.shedule.dto;

import java.util.List;

public record ScheduleResult(
        List<ScheduleResponse> schedules
) {
}
