package softwar7.mapper.shedule.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import softwar7.domain.schedule.vo.Approval;

import java.util.Arrays;

public record ScheduleApproveRequest(
        @Min(value = 1, message = "스케줄 id를 입력해주세요")
        long id,
        @NotNull(message = "스케줄 상태를 입력해주세요")
        Approval approval
) {
        @AssertTrue(message = "잘못된 권한 값입니다")
        private boolean isRoleTypeValid() {
                return approval != null && Arrays.asList(Approval.values()).contains(approval);
        }
}
