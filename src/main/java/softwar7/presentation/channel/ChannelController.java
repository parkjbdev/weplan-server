package softwar7.presentation.channel;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import softwar7.application.channel.ChannelCreateService;
import softwar7.application.channel.ChannelDeleteService;
import softwar7.application.channel.ChannelFindService;
import softwar7.application.channel.ChannelUpdateService;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.vo.MemberSession;
import softwar7.global.annotation.AdminLogin;
import softwar7.global.annotation.Login;
import softwar7.mapper.channel.dto.ChannelResponse;
import softwar7.mapper.channel.dto.ChannelResponses;
import softwar7.mapper.channel.dto.ChannelSaveRequest;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ChannelController {

    private final ChannelCreateService channelCreateService;
    private final ChannelFindService channelFindService;
    private final ChannelDeleteService channelDeleteService;
    private final ChannelUpdateService channelUpdateService;

    public ChannelController(final ChannelCreateService channelCreateService,
                             final ChannelFindService channelFindService,
                             final ChannelDeleteService channelDeleteService,
                             final ChannelUpdateService channelUpdateService) {
        this.channelCreateService = channelCreateService;
        this.channelFindService = channelFindService;
        this.channelDeleteService = channelDeleteService;
        this.channelUpdateService = channelUpdateService;
    }

    @PostMapping("/admin/channels")
    public void createChannel(@AdminLogin final MemberSession memberSession,
                              @RequestBody @Valid final ChannelSaveRequest dto) {
        channelCreateService.createChannel(memberSession.id(), dto);
    }

    @GetMapping("/guest/channels/{channelId}")
    public ChannelResponse getChannel(@Login final MemberSession memberSession,
                                      @PathVariable final long channelId) {
        return channelFindService.findChannel(channelId);
    }

    @GetMapping("/guest/channels")
    public ChannelResponses getChannels(@Login final MemberSession memberSession) {
        List<ChannelResponse> channelResponses = channelFindService.findAll();
        return new ChannelResponses(channelResponses);
    }

    @DeleteMapping("/admin/channels/{channelId}")
    public void deleteChannel(@AdminLogin final MemberSession memberSession,
                              @PathVariable final long channelId) {
        channelDeleteService.deleteChannel(memberSession.id(), channelId);
    }

    @PatchMapping("/admin/channels/{channelId}")
    public void updateChannel(@AdminLogin final MemberSession memberSession,
                              @PathVariable final long channelId,
                              @RequestBody final ChannelUpdateRequest channelUpdateRequest) {
        channelUpdateService.updateChannel(memberSession.id(), channelId, channelUpdateRequest);
    }
}
