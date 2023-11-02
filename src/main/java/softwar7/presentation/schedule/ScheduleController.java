package softwar7.presentation.schedule;

import org.springframework.web.bind.annotation.*;
import softwar7.application.schedule.ScheduleCreateService;
import softwar7.application.schedule.ScheduleFindService;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.annotation.Login;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;

@RequestMapping("/api")
@RestController
public class ScheduleController {

    private final ScheduleCreateService scheduleCreateService;
    private final ScheduleFindService scheduleFindService;

    public ScheduleController(final ScheduleCreateService scheduleCreateService,
                              final ScheduleFindService scheduleFindService) {
        this.scheduleCreateService = scheduleCreateService;
        this.scheduleFindService = scheduleFindService;
    }

    @PostMapping("/schedules")
    public void createSchedule(@Login final MemberSession memberSession,
                               @RequestBody final ScheduleSaveRequest dto) {
        scheduleCreateService.create(memberSession, dto);
    }

    @GetMapping("/schedules/{scheduleId}")
    public ScheduleResponse getSchedules(@PathVariable final long scheduleId) {
        Schedule schedule = scheduleFindService.getById(scheduleId);
        return ScheduleMapper.toResponse(schedule);
    }
}
