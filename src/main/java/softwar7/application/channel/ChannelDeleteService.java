package softwar7.application.channel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.channel.Channel;
import softwar7.repository.channel.ChannelRepository;

@Service
public class ChannelDeleteService {

    private final ChannelRepository channelRepository;

    public ChannelDeleteService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public void deleteChannel(final long channelId){
        Channel channel = channelRepository.getById(channelId);
        channelRepository.delete(channel);
    }
}