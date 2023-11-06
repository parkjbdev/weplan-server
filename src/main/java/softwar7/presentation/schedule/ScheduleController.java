package softwar7.presentation.schedule;

import org.springframework.web.bind.annotation.*;
import softwar7.application.schedule.ScheduleCreateService;
import softwar7.application.schedule.ScheduleFindService;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.global.annotation.AdminLogin;
import softwar7.global.annotation.Login;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.mapper.shedule.dto.ScheduleResult;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;

import java.time.LocalDate;
import java.util.List;

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
    public ScheduleResponse getSchedule(@Login final MemberSession memberSession,
                                        @PathVariable final long scheduleId) {
        Schedule schedule = scheduleFindService.getById(scheduleId);
        return ScheduleMapper.toResponse(schedule);
    }

    @GetMapping("/schedules")
    public ScheduleResult getSchedules(@Login final MemberSession memberSession,
                                       @RequestParam final LocalDate start,
                                       @RequestParam final LocalDate end,
                                       @RequestParam final long channelId) {
        List<ScheduleResponse> scheduleResponses = scheduleFindService.findAllSchedulesByDate(start, end, channelId);
        return new ScheduleResult(scheduleResponses);
    }

    @GetMapping("/admin/schedules/requests")
    public ScheduleResult request(@AdminLogin final MemberSession memberSession) {
        List<ScheduleResponse> scheduleResponses = scheduleFindService.findAllRequestSchedules();
        return new ScheduleResult(scheduleResponses);
    }
}
