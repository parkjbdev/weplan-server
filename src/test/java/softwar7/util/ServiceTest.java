package softwar7.util;

import org.springframework.beans.factory.annotation.Autowired;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.member.MemberRepository;

@AcceptanceTest
public abstract class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ChannelRepository channelRepository;
}
