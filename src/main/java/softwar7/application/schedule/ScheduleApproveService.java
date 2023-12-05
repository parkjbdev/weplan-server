package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.BadRequestException;
import softwar7.mapper.shedule.dto.ScheduleApproveRequest;
import softwar7.repository.schedule.ScheduleRepository;

import static softwar7.global.constant.ExceptionMessage.*;

@Service
public class ScheduleApproveService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleApproveService(final ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void approveSchedule(final ScheduleApproveRequest dto) {
        Schedule schedule = scheduleRepository.getById(dto.id());
        boolean isNotOverlapping =
                scheduleRepository.isNotOverlapping(schedule.getChannelId(),
                        schedule.getStartTime(), schedule.getEndTime());

        if (!isNotOverlapping) {
            throw new BadRequestException(ALREADY_EXIST_SCHEDULE.message);
        }

        schedule.updateApproval(dto.approval());
    }
}
