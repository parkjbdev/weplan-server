package softwar7.mapper.channel.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChannelSaveRequest(
        @Size(min = 1, max = 10, message = "채널명은 1자 이상 10자 이하를 지원합니다")
        String name,
        @Size(min = 1, max = 10, message = "장소 정보는 1자 이상 11자 이하를 지원합니다")
        String place
) {
}
