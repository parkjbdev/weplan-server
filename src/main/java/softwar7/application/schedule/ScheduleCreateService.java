package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.schedule.ScheduleRepository;

import java.util.List;

import static softwar7.global.constant.ExceptionMessage.*;
import static softwar7.global.constant.ExceptionMessage.ALREADY_EXIST_SCHEDULE;

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
        if (dto.start().isAfter(dto.end())) {
            throw new BadRequestException(BAD_REQUEST_SCHEDULE_TIME.message);
        }

        channelRepository.getById(dto.channelId());
        if (scheduleRepository.isNotOverlapping(dto.channelId(), dto.start(), dto.end())) {
            Schedule schedule = ScheduleMapper.toEntity(memberSession, dto);
            scheduleRepository.save(schedule);
        } else {
            throw new BadRequestException(ALREADY_EXIST_SCHEDULE.message);
        }
    }
}
