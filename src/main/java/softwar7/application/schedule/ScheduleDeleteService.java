package softwar7.application.schedule;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.ForbiddenException;
import softwar7.repository.schedule.ScheduleRepository;

import static softwar7.global.constant.ExceptionMessage.*;

@Service
public class ScheduleDeleteService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDeleteService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void deleteSchedule(final long memberId, final long scheduleId){
        Schedule schedule = scheduleRepository.getById(scheduleId);
        if (memberId != schedule.getMemberId()) {
            throw new ForbiddenException(SCHEDULE_FORBIDDEN_EXCEPTION.message);
        }
        scheduleRepository.delete(schedule);
    }
}
