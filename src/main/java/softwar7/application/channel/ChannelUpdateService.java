package softwar7.application.channel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.channel.Channel;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.ForbiddenException;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;
import softwar7.repository.channel.ChannelRepository;

import static softwar7.global.constant.ExceptionMessage.*;

@Service
public class ChannelUpdateService {
    private final ChannelRepository channelRepository;

    public ChannelUpdateService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public void updateChannel(final long memberId, final long channelId,
                              final ChannelUpdateRequest channelUpdateRequest){
        Channel channel = channelRepository.getById(channelId);
        if (memberId != channel.getMemberId()) {
            throw new ForbiddenException(CHANNEL_FORBIDDEN_EXCEPTION.message);
        }
        channel.update(channelUpdateRequest);
    }
}