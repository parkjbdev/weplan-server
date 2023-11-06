package softwar7.application.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.mapper.shedule.dto.ScheduleApproveRequest;
import softwar7.repository.schedule.ScheduleRepository;

@Service
public class ScheduleApproveService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleApproveService(final ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void approveSchedule(final ScheduleApproveRequest dto) {
        Schedule schedule = scheduleRepository.getById(dto.scheduleId());
        schedule.updateApproval(dto.approval());
    }
}
