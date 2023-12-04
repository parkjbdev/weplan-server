package softwar7.application.channel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.global.exception.ForbiddenException;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ChannelUpdateServiceTest extends ServiceTest {

    @Autowired
    private ChannelUpdateService channelUpdateService;

    @DisplayName("관리자 권한으로 해당 채널 정보를 수정한다.")
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
        channelUpdateService.updateChannel(member.getId(), channel.getId(), channelUpdateRequest);

        // then
        Channel updateChannel = channelRepository.getById(channel.getId());
        assertThat(updateChannel.getChannelName()).isEqualTo("수정 채널명");
        assertThat(updateChannel.getChannelPlace()).isEqualTo("수정 채널 장소");
    }

    @DisplayName("채널 수정 권한이 없으면 예외가 발생한다.")
    @Test
    void updateChannelFail() {
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
        assertThatThrownBy(() ->
                channelUpdateService.updateChannel(9999L, channel.getId(), channelUpdateRequest))
                .isInstanceOf(ForbiddenException.class);
    }
}