package softwar7.application.channel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChannelFindServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChannelRepository channelRepository;

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
}