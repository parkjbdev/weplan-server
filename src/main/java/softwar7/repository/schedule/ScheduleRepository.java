package softwar7.repository.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import softwar7.domain.schedule.persist.QSchedule;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.domain.schedule.vo.Approval;
import softwar7.global.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static softwar7.domain.schedule.persist.QSchedule.schedule;
import static softwar7.global.constant.ExceptionMessage.SCHEDULE_NOT_FOUND_EXCEPTION;

@Repository
public class ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;
    private final JPAQueryFactory queryFactory;

    public ScheduleRepository(final ScheduleJpaRepository scheduleJpaRepository, final JPAQueryFactory queryFactory) {
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.queryFactory = queryFactory;
    }

    public void save(final Schedule schedule) {
        scheduleJpaRepository.save(schedule);
    }

    public Schedule getById(final long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION.message));
    }

    public List<Schedule> findAllSchedulesByDate(final LocalDate start, final LocalDate end,
                                                 final long channelId) {
        LocalDateTime startDateTime = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);

        return queryFactory.selectFrom(schedule)
                .where(
                        schedule.channelId.eq(channelId)
                                .and(schedule.startTime.after(startDateTime))
                                .and(schedule.endTime.before(endDateTime))
                                .and(schedule.approval.eq(Approval.APPROVED))
                )
                .fetch();
    }

    public List<Schedule> findAllByChannelId(final long channelId) {
        return queryFactory.selectFrom(schedule)
                .where(
                        schedule.channelId.eq(channelId)
                                .and(schedule.approval.eq(Approval.APPROVED))
                )
                .fetch();
    }

    public List<Schedule> findAllRequestSchedules() {
        return queryFactory.selectFrom(schedule)
                .where(schedule.approval.eq(Approval.PENDING))
                .fetch();
    }

    public List<Schedule> findAllMemberSchedules(final LocalDate start, final LocalDate end,
                                                 final Approval approval, final long memberId) {
        return queryFactory.select(schedule)
                .where(schedule.memberId.eq(memberId)
                        .and(schedule.startTime.between(LocalDateTime.from(start), LocalDateTime.from(end))
                                .and(schedule.endTime.between(LocalDateTime.from(start), LocalDateTime.from(end))
                                        .and(schedule.approval.eq(approval)))
                        )
                )
                .fetch();
    }

    public List<Schedule> findAllByMemberId(final long memberId, final Approval approval) {
        return queryFactory.select(schedule)
                .where(schedule.memberId.eq(memberId)
                        .and(schedule.approval.eq(approval)))
                .fetch();
    }
}
