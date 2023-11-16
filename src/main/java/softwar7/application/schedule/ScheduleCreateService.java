package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.schedule.ScheduleRepository;

@Service
public class ScheduleCreateService {

    private final ChannelRepository channelRepository;
    private final ScheduleRepository scheduleRepository;

    public ScheduleCreateService(final ChannelRepository channelRepository,
                                 final ScheduleRepository scheduleRepository) {
        this.channelRepository = channelRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void create(final MemberSession memberSession, final ScheduleSaveRequest dto) {
        channelRepository.getById(dto.channelId());
        Schedule schedule = ScheduleMapper.toEntity(memberSession, dto);
        scheduleRepository.save(schedule);
    }
}
