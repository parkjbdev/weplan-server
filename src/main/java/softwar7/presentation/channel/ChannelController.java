package softwar7.presentation.channel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwar7.application.channel.ChannelCreateService;
import softwar7.domain.member.vo.MemberSession;
import softwar7.global.annotation.Login;
import softwar7.mapper.channel.dto.ChannelSaveRequest;

@RequestMapping("/api")
@RestController
public class ChannelController {

    private final ChannelCreateService channelCreateService;

    public ChannelController(final ChannelCreateService channelCreateService) {
        this.channelCreateService = channelCreateService;
    }

    @PostMapping("/admin/channels")
    public void createChannel(@Login final MemberSession memberSession,
                              @RequestBody final ChannelSaveRequest dto) {
        channelCreateService.createChannel(memberSession.id(), dto);
    }
}
