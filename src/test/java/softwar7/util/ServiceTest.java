package softwar7.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.jwt.JwtRefreshTokenRepository;
import softwar7.repository.member.MemberRepository;
import softwar7.repository.schedule.ScheduleRepository;

@AcceptanceTest
public abstract class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ChannelRepository channelRepository;

    @Autowired
    protected ScheduleRepository scheduleRepository;

    @Autowired
    protected JwtRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;
}
