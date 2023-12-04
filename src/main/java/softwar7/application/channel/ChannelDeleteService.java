package softwar7.application.channel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.channel.Channel;
import softwar7.global.exception.ForbiddenException;
import softwar7.repository.channel.ChannelRepository;

import static softwar7.global.constant.ExceptionMessage.CHANNEL_FORBIDDEN_EXCEPTION;

@Service
public class ChannelDeleteService {

    private final ChannelRepository channelRepository;

    public ChannelDeleteService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public void deleteChannel(final long memberId, final long channelId){
        Channel channel = channelRepository.getById(channelId);
        if (memberId != channel.getMemberId()) {
            throw new ForbiddenException(CHANNEL_FORBIDDEN_EXCEPTION.message);
        }
        channelRepository.delete(channel);
    }
}