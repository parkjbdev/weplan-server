package softwar7.repository.schedule;

import org.springframework.stereotype.Repository;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.constant.ExceptionMessage;
import softwar7.global.exception.NotFoundException;

import static softwar7.global.constant.ExceptionMessage.SCHEDULE_NOT_FOUND_EXCEPTION;

@Repository
public class ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    public ScheduleRepository(final ScheduleJpaRepository scheduleJpaRepository) {
        this.scheduleJpaRepository = scheduleJpaRepository;
    }

    public void save(final Schedule schedule) {
        scheduleJpaRepository.save(schedule);
    }

    public Schedule getById(final long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION.message));
    }
}
