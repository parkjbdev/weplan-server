package softwar7.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import softwar7.domain.schedule.persist.Schedule;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(final long memberId);
}
