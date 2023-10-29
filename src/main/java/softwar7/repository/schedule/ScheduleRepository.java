package softwar7.repository.schedule;

import org.springframework.stereotype.Repository;
import softwar7.domain.schedule.persist.Schedule;

@Repository
public class ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    public ScheduleRepository(final ScheduleJpaRepository scheduleJpaRepository) {
        this.scheduleJpaRepository = scheduleJpaRepository;
    }

    public void save(final Schedule schedule) {
        scheduleJpaRepository.save(schedule);
    }
}
