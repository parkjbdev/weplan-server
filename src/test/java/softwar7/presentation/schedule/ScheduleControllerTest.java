package softwar7.presentation.schedule;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.domain.schedule.persist.Schedule;
import softwar7.domain.schedule.vo.Approval;
import softwar7.mapper.channel.dto.ChannelUpdateRequest;
import softwar7.mapper.shedule.dto.ScheduleApproveRequest;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;
import softwar7.mapper.shedule.dto.ScheduleUpdateRequest;
import softwar7.util.ControllerTest;

import java.time.LocalDateTime;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;

class ScheduleControllerTest extends ControllerTest {

    @Test
    @DisplayName("스케줄 예약 요청")
    void createSchedule() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        ScheduleSaveRequest dto = ScheduleSaveRequest.builder()
                .name("스케줄명")
                .content("스케줄 내용")
                .start(LocalDateTime.of(2023, 10, 10, 10, 0))
                .end(LocalDateTime.of(2023, 10, 10, 11, 0))
                .channelId(channel.getId())
                .build();

        // expected
        mockMvc.perform(post("/api/guest/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("스케줄 생성",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 생성")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("name").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("content").type(STRING).description("스케줄 내용"),
                                        fieldWithPath("start").type(STRING).description("시작 시간"),
                                        fieldWithPath("end").type(STRING).description("종료 시간"),
                                        fieldWithPath("channelId").type(NUMBER).description("채널 ID")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 예약 요청 - 중복된 스케줄")
    void createScheduleFail() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        Schedule schedule = Schedule.builder()
                .channelId(channel.getId())
                .startTime(LocalDateTime.of(2023, 12, 1, 0, 0, 0))
                .endTime(LocalDateTime.of(2023, 12, 2, 0, 0, 0))
                .build();

        scheduleRepository.save(schedule);

        ScheduleSaveRequest dto = ScheduleSaveRequest.builder()
                .name("스케줄명")
                .content("스케줄 내용")
                .start(LocalDateTime.of(2023, 12, 1, 0, 0))
                .end(LocalDateTime.of(2023, 12, 2, 0, 0))
                .channelId(channel.getId())
                .build();

        // expected
        mockMvc.perform(post("/api/guest/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andDo(document("스케줄 생성 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 생성")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("name").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("content").type(STRING).description("스케줄 내용"),
                                        fieldWithPath("start").type(STRING).description("시작 시간"),
                                        fieldWithPath("end").type(STRING).description("종료 시간"),
                                        fieldWithPath("channelId").type(NUMBER).description("채널 ID")
                                )
                                .responseFields(
                                        fieldWithPath("statusCode").type(STRING).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("예외 메세지")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("단일 스케줄 조회에 성공")
    void getSchedule() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명")
                .content("스케줄 내용")
                .startTime(LocalDateTime.of(2023, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 10, 11, 0))
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule);

        // expected
        mockMvc.perform(get("/api/guest/schedules/{scheduleId}", schedule.getId())
                        .header(ACCESS_TOKEN.value, accessToken)
                        .param("start", "2023-10-10T00:00:00")
                        .param("end", "2023-10-12T23:59:59"))
                .andExpect(status().isOk())
                .andDo(document("단일 스케줄 조회",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("단일 스케줄 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .responseFields(
                                        fieldWithPath("id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("name").type(STRING).description("스케줄명"),
                                        fieldWithPath("content").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("start").type(STRING).description("시작 시간"),
                                        fieldWithPath("end").type(STRING).description("종료 시간"),
                                        fieldWithPath("channelId").type(NUMBER).description("채널 ID"),
                                        fieldWithPath("approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("일정에 맞는 스케줄 조회")
    void getSchedules() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        createSchedules(member, channel);
        String channelId = String.valueOf(channel.getId());

        // expected
        mockMvc.perform(get("/api/guest/schedules")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .param("start", "2023-10-10T00:00:00")
                        .param("end", "2023-10-12T23:59:59")
                        .param("channelId", channelId))
                .andExpect(status().isOk())
                .andDo(document("해당 일정의 스케줄 조회",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("해당 일정의 스케줄 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .queryParameters(
                                        RequestDocumentation.parameterWithName("start").description("시작 시간"),
                                        RequestDocumentation.parameterWithName("end").description("종료 시간"),
                                        RequestDocumentation.parameterWithName("channelId").description("채널 id")
                                )
                                .responseFields(
                                        fieldWithPath("schedules[].id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("schedules[].name").type(STRING).description("스케줄명"),
                                        fieldWithPath("schedules[].content").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("schedules[].start").type(STRING).description("시작 시간"),
                                        fieldWithPath("schedules[].end").type(STRING).description("종료 시간"),
                                        fieldWithPath("schedules[].channelId").type(NUMBER).description("채널 ID"),
                                        fieldWithPath("schedules[].approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("사용자의 스케줄 신청 목록 조회")
    void getSchedulesRequest() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        createSchedules(member, channel);

        // expected
        mockMvc.perform(get("/api/guest/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .param("approval", String.valueOf(Approval.APPROVED))
                        .param("start", "2023-10-10T00:00:00")
                        .param("end", "2023-10-12T23:59:59")
                )
                .andExpect(status().isOk())
                .andDo(document("게스트의 요청 스케줄 목록 조회",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("게스트의 요청 스케줄 목록 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .queryParameters(
                                        RequestDocumentation.parameterWithName("approval").description("승인 여부"),
                                        RequestDocumentation.parameterWithName("start").description("시작 시간"),
                                        RequestDocumentation.parameterWithName("end").description("종료 시채")
                                )
                                .responseFields(
                                        fieldWithPath("schedules[].id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("schedules[].name").type(STRING).description("스케줄명"),
                                        fieldWithPath("schedules[].content").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("schedules[].start").type(STRING).description("시작 시간"),
                                        fieldWithPath("schedules[].end").type(STRING).description("종료 시간"),
                                        fieldWithPath("schedules[].channelId").type(NUMBER).description("채널 ID"),
                                        fieldWithPath("schedules[].approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 등록을 요청한 스케줄 목록 조회")
    void getRequestSchedules() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        createPendingSchedules(member, channel);

        // expected
        mockMvc.perform(get("/api/admin/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken))
                .andExpect(status().isOk())
                .andDo(document("스케줄 등록 요청 목록 조회",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 등록 요청 목록 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .responseFields(
                                        fieldWithPath("schedules[].id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("schedules[].name").type(STRING).description("스케줄명"),
                                        fieldWithPath("schedules[].content").type(STRING).description("스케줄 이름"),
                                        fieldWithPath("schedules[].start").type(STRING).description("시작 시간"),
                                        fieldWithPath("schedules[].end").type(STRING).description("종료 시간"),
                                        fieldWithPath("schedules[].channelId").type(NUMBER).description("채널 ID"),
                                        fieldWithPath("schedules[].approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 등록을 요청한 스케줄을 승인")
    void approveRequestSchedules() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명3")
                .content("스케줄 내용3")
                .startTime(LocalDateTime.of(2023, 10, 12, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 12, 11, 0))
                .approval(Approval.PENDING)
                .build();

        scheduleRepository.save(schedule);
        ScheduleApproveRequest dto = new ScheduleApproveRequest(schedule.getId(), Approval.APPROVED);

        // expected
        mockMvc.perform(post("/api/admin/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("스케줄 등록 요청 승인",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 등록 요청 승인")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 등록을 요청한 스케줄을 거절")
    void rejectRequestSchedules() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명3")
                .content("스케줄 내용3")
                .startTime(LocalDateTime.of(2023, 10, 12, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 12, 11, 0))
                .approval(Approval.PENDING)
                .build();

        scheduleRepository.save(schedule);
        ScheduleApproveRequest dto = new ScheduleApproveRequest(schedule.getId(), Approval.REJECTED);

        // expected
        mockMvc.perform(post("/api/admin/schedules/requests")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("스케줄 등록 요청 거절",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 등록 요청 거절")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("id").type(NUMBER).description("스케줄 ID"),
                                        fieldWithPath("approval").type(STRING).description("승인 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 업데이트")
    void updateSchedule() throws Exception {
        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);


        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .build();

        scheduleRepository.save(schedule);

        // given 3
        ScheduleUpdateRequest dto = ScheduleUpdateRequest.builder()
                .name("수정 스케줄명")
                .content("수정 스케줄 내용")
                .build();

        // expected
        mockMvc.perform(patch("/api/guest/schedules/{scheduleId}", schedule.getId())
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("스케줄 수정 성공",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 수정")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("name").type(STRING).description("스케줄명"),
                                        fieldWithPath("content").type(STRING).description("스케줄 내용")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("권한 없는 사용자가 스케줄 삭제")
    void deleteScheduleFail() throws Exception {

        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        Schedule schedule = Schedule.builder()
                .memberId(9999L)
                .channelId(channel.getId())
                .build();

        scheduleRepository.save(schedule);

        // expected
        mockMvc.perform(delete("/api/guest/schedules/{scheduleId}", schedule.getId())
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andDo(document("스케줄 삭제 - 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                RequestDocumentation.parameterWithName("scheduleId").description("스케줄 id")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 삭제")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .responseFields(
                                        fieldWithPath("statusCode").type(STRING).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("예외 메세지")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("스케줄 삭제 성공")
    void deleteSchedule() throws Exception {

        // given 1
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        // given 2
        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .username("사용자 이름")
                .roleType(RoleType.ADMIN)
                .build();

        String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")

                .build();

        channelRepository.save(channel);

        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .build();

        scheduleRepository.save(schedule);

        // expected
        mockMvc.perform(delete("/api/guest/schedules/{scheduleId}", schedule.getId())
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("스케줄 삭제",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                RequestDocumentation.parameterWithName("scheduleId").description("스케줄 id")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("스케줄")
                                .summary("스케줄 삭제")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .build()
                        )));
    }

    private void createSchedules(final Member member, final Channel channel) {
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명1")
                .content("스케줄 내용1")
                .startTime(LocalDateTime.of(2023, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 10, 11, 0))
                .approval(Approval.APPROVED)
                .build();

        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명2")
                .content("스케줄 내용2")
                .startTime(LocalDateTime.of(2023, 10, 11, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 11, 11, 0))
                .approval(Approval.APPROVED)
                .build();

        Schedule schedule3 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명3")
                .content("스케줄 내용3")
                .startTime(LocalDateTime.of(2023, 10, 12, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 12, 11, 0))
                .approval(Approval.APPROVED)
                .build();

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
    }

    private void createPendingSchedules(final Member member, final Channel channel) {
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명1")
                .content("스케줄 내용1")
                .startTime(LocalDateTime.of(2023, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 10, 11, 0))
                .approval(Approval.PENDING)
                .build();

        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명2")
                .content("스케줄 내용2")
                .startTime(LocalDateTime.of(2023, 10, 11, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 11, 11, 0))
                .approval(Approval.PENDING)
                .build();

        Schedule schedule3 = Schedule.builder()
                .memberId(member.getId())
                .channelId(channel.getId())
                .username(member.getUsername())
                .scheduleName("스케줄명3")
                .content("스케줄 내용3")
                .startTime(LocalDateTime.of(2023, 10, 12, 10, 0))
                .endTime(LocalDateTime.of(2023, 10, 12, 11, 0))
                .approval(Approval.PENDING)
                .build();

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
    }
}