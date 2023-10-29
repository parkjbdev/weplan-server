package softwar7.mapper.channel;

import softwar7.domain.channel.Channel;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.mapper.channel.dto.ChannelSaveRequest;

public enum ChannelMapper {

    ChannelMapper(){
    };

    public static Channel toEntity(final long memberId, final ChannelSaveRequest dto) {
        return Channel.builder()
                .memberId(memberId)
                .channelName(dto.name())
                .channelPlace(dto.place())
                .build();
    }

    public static ChannelResponse toChannelResponse(final Channel channel, final String username) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getChannelName())
                .place(channel.getChannelPlace())
                .createdBy(username)
                .build();
    }
}
