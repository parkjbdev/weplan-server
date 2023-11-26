package softwar7.domain.channel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;


class ChannelTest {

    @DisplayName("채널의 정보(채널명, 채널 장소)를 수정한다.")
    @Test
    void update() {
        // given
        Channel channel = Channel.builder()
                .memberId(1L)
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        ChannelUpdateRequest dto = ChannelUpdateRequest.builder()
                .name("수정 채널명")
                .place("수정 채널 장소")
                .build();

        // when
        channel.update(dto);

        // then
        Assertions.assertThat(channel.getChannelName()).isEqualTo(dto.name());
        Assertions.assertThat(channel.getChannelPlace()).isEqualTo(dto.place());
    }
}