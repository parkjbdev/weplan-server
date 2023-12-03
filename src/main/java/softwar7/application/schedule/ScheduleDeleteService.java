package softwar7.application.schedule;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.repository.schedule.ScheduleRepository;

@Service
public class ScheduleDeleteService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDeleteService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void deleteSchedule(final long scheduleId){
        Schedule schedule = scheduleRepository.getById(scheduleId);
        scheduleRepository.delete(schedule);
    }
}
