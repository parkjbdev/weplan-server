package softwar7.application.channel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.global.exception.NotFoundException;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.util.ServiceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChannelFindServiceTest extends ServiceTest {

    @Autowired
    private ChannelFindService channelFindService;

    @DisplayName("채널 id로 해당 채널 정보를 가져온다.")
    @Test
    void findChannel() {
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

        // when
        ChannelResponse channelResponse = channelFindService.findChannel(channel.getId());

        // then
        assertThat(channelResponse.id()).isEqualTo(channel.getId());
        assertThat(channelResponse.name()).isEqualTo(channel.getChannelName());
        assertThat(channelResponse.place()).isEqualTo(channel.getChannelPlace());
        assertThat(channelResponse.createdBy()).isEqualTo(member.getUsername());
    }

    @DisplayName("채널 id와 관련된 채널이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findChannelFail() {
        // given 1
        Member member = Member.builder()
                .username("회원 이름")
                .build();

        memberRepository.save(member);

        // expected
        assertThatThrownBy(() -> channelFindService.findChannel(9999))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("모든 채널의 정보를 가져온다.")
    @Test
    void findAll() {
        // given 1
        Member member = Member.builder()
                .username("회원 이름")
                .build();

        memberRepository.save(member);

        // given 2
        Channel channel1 = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명1")
                .channelPlace("채널 장소2")
                .build();

        Channel channel2 = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명1")
                .channelPlace("채널 장소2")
                .build();

        channelRepository.save(channel1);
        channelRepository.save(channel2);

        // when
        List<ChannelResponse> channelResponses = channelFindService.findAll();

        // then
        assertThat(channelResponses.size()).isEqualTo(2);
    }
}