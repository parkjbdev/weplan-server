package softwar7.presentation.schedule;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwar7.application.schedule.ScheduleCreateService;
import softwar7.domain.member.vo.MemberSession;
import softwar7.global.annotation.Login;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;

@RequestMapping("/api")
@RestController
public class ScheduleController {

    private final ScheduleCreateService scheduleCreateService;

    public ScheduleController(final ScheduleCreateService scheduleCreateService) {
        this.scheduleCreateService = scheduleCreateService;
    }

    @PostMapping("/admin/schedules")
    public void createSchedule(@Login final MemberSession memberSession,
                               @RequestBody final ScheduleSaveRequest dto) {
        scheduleCreateService.create(memberSession, dto);
    }
}
