package softwar7.application.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.global.exception.ForbiddenException;
import softwar7.global.exception.NotFoundException;
import softwar7.util.ServiceTest;

import static org.assertj.core.api.Assertions.*;

class ChannelDeleteServiceTest extends ServiceTest {

    @Autowired
    private ChannelDeleteService channelDeleteService;

    @DisplayName("서버에 등록된 채널 ID로 채널 삭제")
    @Test
    void deleteChannel() {
        // given
        Member member = Member.builder()
                .username("회원 이름")
                .build();

        memberRepository.save(member);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널 이름")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // when
        channelDeleteService.deleteChannel(member.getId(), channel.getId());

        // then
        assertThat(channelRepository.count()).isEqualTo(0);
    }

    @DisplayName("서버에 등록되지 않은 채널 ID로 채널 삭제")
    @Test
    void deleteChannelNotFound() {
        // expected
        assertThatThrownBy(() ->
                channelDeleteService.deleteChannel(9999L, 9999L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("채널 삭제 권한이 없는 관리자가 채널 삭제")
    @Test
    void deleteChannelForbidden() {
        // given
        Member member = Member.builder()
                .username("회원 이름")
                .build();

        memberRepository.save(member);

        Channel channel = Channel.builder()
                .memberId(1L)
                .channelName("채널 이름")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // expected
        assertThatThrownBy(() ->
                channelDeleteService.deleteChannel(9999L, channel.getId()))
                .isInstanceOf(ForbiddenException.class);
    }
}