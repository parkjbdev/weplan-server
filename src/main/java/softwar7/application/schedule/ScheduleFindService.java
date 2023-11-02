package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.repository.schedule.ScheduleRepository;

@Service
public class ScheduleFindService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleFindService(final ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = true)
    public Schedule getById(final long scheduleId) {
        return scheduleRepository.getById(scheduleId);
    }
}
