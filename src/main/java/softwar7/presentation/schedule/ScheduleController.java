package softwar7.presentation.schedule;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import softwar7.application.schedule.ScheduleApproveService;
import softwar7.application.schedule.ScheduleCreateService;
import softwar7.application.schedule.ScheduleFindService;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.domain.schedule.vo.Approval;
import softwar7.global.annotation.AdminLogin;
import softwar7.global.annotation.Login;
import softwar7.mapper.shedule.ScheduleMapper;
import softwar7.mapper.shedule.dto.ScheduleApproveRequest;
import softwar7.mapper.shedule.dto.ScheduleResponse;
import softwar7.mapper.shedule.dto.ScheduleResult;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ScheduleController {

    private final ScheduleCreateService scheduleCreateService;
    private final ScheduleFindService scheduleFindService;
    private final ScheduleApproveService scheduleApproveService;

    public ScheduleController(final ScheduleCreateService scheduleCreateService,
                              final ScheduleFindService scheduleFindService,
                              final ScheduleApproveService scheduleApproveService) {
        this.scheduleCreateService = scheduleCreateService;
        this.scheduleFindService = scheduleFindService;
        this.scheduleApproveService = scheduleApproveService;
    }

    @PostMapping("/guest/schedules")
    public void createSchedule(@Login final MemberSession memberSession,
                               @RequestBody final ScheduleSaveRequest dto) {
        scheduleCreateService.create(memberSession, dto);
    }

    @GetMapping("/guest/schedules/{scheduleId}")
    public ScheduleResponse getSchedule(@Login final MemberSession memberSession,
                                        @PathVariable final long scheduleId) {
        Schedule schedule = scheduleFindService.getById(scheduleId);
        return ScheduleMapper.toResponse(schedule);
    }

    @GetMapping("/guest/schedules")
    public ScheduleResult getSchedules(
            @Login final MemberSession memberSession,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime end,
            @RequestParam(required = false) final long channelId
    ) {

        List<ScheduleResponse> scheduleResponses = scheduleFindService.findAllSchedulesByDate(start, end, channelId);
        return new ScheduleResult(scheduleResponses);
    }

    @GetMapping("/guest/schedules/requests")
    public List<ScheduleResponse> getRequestSchedules(
            @Login final MemberSession memberSession,
            @RequestParam(required = false) final Approval approval,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime end
    ) {
        return scheduleFindService.findAllMemberSchedules(start, end, approval, memberSession.id());
    }

    @GetMapping("/admin/schedules/requests")
    public ScheduleResult findPendingSchedules(@AdminLogin final MemberSession memberSession) {
        List<ScheduleResponse> scheduleResponses = scheduleFindService.findAllRequestSchedules();
        return new ScheduleResult(scheduleResponses);
    }

    @PostMapping("/admin/schedules/requests")
    public void approveSchedule(@AdminLogin final MemberSession memberSession,
                                @RequestBody final ScheduleApproveRequest dto) {
        scheduleApproveService.approveSchedule(dto);
    }
}
