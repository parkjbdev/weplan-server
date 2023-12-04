package softwar7.application.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.*;

class ChannelDeleteServiceTest extends ServiceTest {

    @Autowired
    private ChannelDeleteService channelDeleteService;

    @DisplayName("채널 id로 특정 채널을 삭제한다")
    @Test
    void deleteChannel() {
        // given
        Channel channel = Channel.builder()
                .memberId(1L)
                .channelName("채널 이름")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // when
        channelDeleteService.deleteChannel(channel.getId());

        // then
        assertThat(channelRepository.count()).isEqualTo(0);
    }
}