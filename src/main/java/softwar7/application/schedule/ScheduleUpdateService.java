package softwar7.application.schedule;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.mapper.shedule.dto.ScheduleUpdateRequest;
import softwar7.repository.schedule.ScheduleRepository;


@Service
public class ScheduleUpdateService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleUpdateService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void updateSchedule(final long scheduleId, final ScheduleUpdateRequest dto){
        Schedule schedule = scheduleRepository.getById(scheduleId);
        schedule.updateSchedule(dto);
    }
}
