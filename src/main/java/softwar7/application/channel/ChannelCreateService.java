package softwar7.application.channel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.channel.Channel;
import softwar7.mapper.channel.ChannelMapper;
import softwar7.mapper.channel.dto.ChannelSaveRequest;
import softwar7.repository.channel.ChannelRepository;

@Service
public class ChannelCreateService {

    private final ChannelRepository channelRepository;

    public ChannelCreateService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public void createChannel(final long memberId, final ChannelSaveRequest dto) {
        Channel channel = ChannelMapper.toEntity(memberId, dto);
        channelRepository.save(channel);
    }
}
