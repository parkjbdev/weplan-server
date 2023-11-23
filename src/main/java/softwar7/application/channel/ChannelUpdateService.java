package softwar7.application.channel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.channel.Channel;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;
import softwar7.repository.channel.ChannelRepository;

@Service
public class ChannelUpdateService {
    private final ChannelRepository channelRepository;

    public ChannelUpdateService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public void updateChannel(final long channelId, final ChannelUpdateRequest channelUpdateRequest){
        Channel channel = channelRepository.getById(channelId);
        channel.update(channelUpdateRequest);
    }
}