package softwar7.application.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChannelUpdateServiceTest extends ServiceTest {

    @Autowired
    private ChannelUpdateService channelUpdateService;

    @DisplayName("채널 id로 해당 채널 정보를 가져온다.")
    @Test
    void updateChannel() {
        // given 1
        Member member = Member.builder()
                .username("회원 이름")
                .build();

        memberRepository.save(member);

        // given 2
        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        ChannelUpdateRequest channelUpdateRequest = ChannelUpdateRequest.builder()
                .name("수정 채널명")
                .place("수정 채널 장소")
                .build();

        // when
        channelUpdateService.updateChannel(channel.getId(), channelUpdateRequest);

        // then
        Channel updateChannel = channelRepository.getById(channel.getId());
        assertThat(updateChannel.getChannelName()).isEqualTo("수정 채널명");
        assertThat(updateChannel.getChannelPlace()).isEqualTo("수정 채널 장소");
    }
}