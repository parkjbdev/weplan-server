package softwar7.repository.channel;

import org.springframework.stereotype.Repository;
import softwar7.domain.channel.Channel;
import softwar7.global.exception.NotFoundException;

import java.util.List;

import static softwar7.global.constant.ExceptionMessage.CHANNEL_NOT_FOUND_EXCEPTION;

@Repository
public class ChannelRepository {

    private final ChannelJpaRepository channelJpaRepository;

    public ChannelRepository(final ChannelJpaRepository channelJpaRepository) {
        this.channelJpaRepository = channelJpaRepository;
    }

    public void save(final Channel channel) {
        channelJpaRepository.save(channel);
    }

    public Channel getById(final long channelId) {
        return channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException(CHANNEL_NOT_FOUND_EXCEPTION.message));
    }

    public List<Channel> findAll() {
        return channelJpaRepository.findAll();
    }
}
