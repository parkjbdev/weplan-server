package softwar7.presentation.channel;

import org.springframework.web.bind.annotation.*;
import softwar7.application.channel.ChannelCreateService;
import softwar7.application.channel.ChannelFindService;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.vo.MemberSession;
import softwar7.global.annotation.Login;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.mapper.channel.dto.ChannelSaveRequest;

@RequestMapping("/api")
@RestController
public class ChannelController {

    private final ChannelCreateService channelCreateService;
    private final ChannelFindService channelFindService;

    public ChannelController(final ChannelCreateService channelCreateService,
                             final ChannelFindService channelFindService) {
        this.channelCreateService = channelCreateService;
        this.channelFindService = channelFindService;
    }

    @PostMapping("/admin/channels")
    public void createChannel(@Login final MemberSession memberSession,
                              @RequestBody final ChannelSaveRequest dto) {
        channelCreateService.createChannel(memberSession.id(), dto);
    }

    @GetMapping("/admin/channels/{channelId}")
    public ChannelResponse getChannel(@PathVariable final long channelId) {
        return channelFindService.findChannel(channelId);
    }
}
