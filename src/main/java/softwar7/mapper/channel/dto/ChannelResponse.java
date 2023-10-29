package softwar7.mapper.channel.dto;

import lombok.Builder;

@Builder
public record ChannelResponse(
        long id,
        String name,
        String place,
        String createdBy
) {
}
