package softwar7.application.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.mapper.channel.dto.ChannelSaveRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;

class ChannelCreateServiceTest extends ServiceTest {

    @Autowired
    private ChannelCreateService channelCreateService;

    @DisplayName("로그인 한 회원이 채널을 생성한다.")
    @Test
    void createChannel() {
        // given
        long memberId = 1;
        ChannelSaveRequest channelSaveRequest = ChannelSaveRequest.builder()
                .name("채널명")
                .place("채널 장소")
                .build();

        // when
        channelCreateService.createChannel(memberId, channelSaveRequest);

        // then
        assertThat(channelRepository.count()).isEqualTo(1);
    }
}