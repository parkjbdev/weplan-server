package softwar7.mapper.shedule.dto;

import softwar7.domain.schedule.vo.Approval;

public record ScheduleApproveRequest(
        long scheduleId,
        Approval approval
) {
}
