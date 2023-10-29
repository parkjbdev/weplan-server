package softwar7.repository.channel;

import org.springframework.stereotype.Repository;
import softwar7.domain.channel.Channel;

@Repository
public class ChannelRepository {

    private final ChannelJpaRepository channelJpaRepository;

    public ChannelRepository(final ChannelJpaRepository channelJpaRepository) {
        this.channelJpaRepository = channelJpaRepository;
    }

    public void save(final Channel channel) {
        channelJpaRepository.save(channel);
    }
}
