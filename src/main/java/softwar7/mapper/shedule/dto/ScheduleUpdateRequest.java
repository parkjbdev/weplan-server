package softwar7.mapper.shedule.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record ScheduleUpdateRequest(
        @Size(min = 1, max = 10, message = "스케줄명은 1자 이상 10자 이하를 지원합니다")
        String name,
        @Size(max = 100, message = "스케줄 내용은 100자 이하를 지원합니다")
        String content
) {
}
