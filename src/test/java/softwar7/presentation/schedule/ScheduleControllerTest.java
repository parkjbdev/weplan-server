package softwar7.presentation.schedule;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.mapper.shedule.dto.ScheduleSaveRequest;
import softwar7.util.ControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;

class ScheduleControllerTest extends ControllerTest {

    @Test
    @DisplayName("스케줄 생성에 성공합니다")
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

        String accessToken = jwtFacade.createAccessToken(memberSession, ONE_HOUR.value);

        Channel channel = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명")
                .channelPlace("채널 장소")
                .build();

        channelRepository.save(channel);

        // given 3
        ScheduleSaveRequest dto = ScheduleSaveRequest.builder()
                .name("스케줄 이름")
                .content("스케줄 내용")
                .start(LocalDateTime.of(2023, 10, 10, 10, 0))
                .end(LocalDateTime.of(2023, 10, 10, 11, 0))
                .channelId(channel.getId())
                .build();

        // expected
        mockMvc.perform(post("/api/schedules")
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
}