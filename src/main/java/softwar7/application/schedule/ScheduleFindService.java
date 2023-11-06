package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.repository.schedule.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllSchedulesByDate(final LocalDate start, final LocalDate end,
                                                         final long channelId) {
        List<Schedule> schedules = scheduleRepository.findAllSchedulesByDate(start, end, channelId);
        return ScheduleMapper.toResponses(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllRequestSchedules() {
        List<Schedule> schedules = scheduleRepository.findAllRequestSchedules();
        return ScheduleMapper.toResponses(schedules);
    }
}
