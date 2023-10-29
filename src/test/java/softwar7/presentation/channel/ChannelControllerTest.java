package softwar7.presentation.channel;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import softwar7.domain.channel.Channel;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.mapper.channel.dto.ChannelSaveRequest;
import softwar7.util.ControllerTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;

class ChannelControllerTest extends ControllerTest {

    @Test
    @DisplayName("로그인 한 관리자가 채널을 생성합니다")
    void createChannel() throws Exception {
        // given
        String accessToken = login();

        ChannelSaveRequest dto = ChannelSaveRequest.builder()
                .name("채널명")
                .place("채널 장소")
                .build();

        // expected
        mockMvc.perform(post("/api/admin/channels")
                        .header(ACCESS_TOKEN.value, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("채널 생성 성공",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("채널")
                                .summary("채널 생성")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .requestFields(
                                        fieldWithPath("name").type(STRING).description("채널명"),
                                        fieldWithPath("place").type(STRING).description("채널 장소")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("단일 채널 조회에 성공합니다")
    void getChannel() throws Exception {
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

        // expected
        mockMvc.perform(get("/api/admin/channels/{channelId}", channel.getId())
                        .header(ACCESS_TOKEN.value, accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("단일 채널 조회",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("channelId").description("채널 id")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("채널")
                                .summary("단일 채널 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .responseFields(
                                        fieldWithPath("id").type(NUMBER).description("채널 id"),
                                        fieldWithPath("name").type(STRING).description("채널명"),
                                        fieldWithPath("place").type(STRING).description("채널 장소"),
                                        fieldWithPath("createdBy").type(STRING).description("채널 주인")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("모든 채널 조회에 성공합니다")
    void getChannels() throws Exception {
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

        Channel channel1 = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명1")
                .channelPlace("채널 장소1")
                .build();

        Channel channel2 = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명1")
                .channelPlace("채널 장소1")
                .build();

        Channel channel3 = Channel.builder()
                .memberId(member.getId())
                .channelName("채널명3")
                .channelPlace("채널 장소3")
                .build();

        channelRepository.save(channel1);
        channelRepository.save(channel2);
        channelRepository.save(channel3);

        // expected
        mockMvc.perform(get("/api/admin/channels")
                        .header(ACCESS_TOKEN.value, accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("모든 채널 조회",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("채널")
                                .summary("모든 채널 조회")
                                .requestHeaders(
                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
                                )
                                .responseFields(
                                        fieldWithPath("channels[].id").type(NUMBER).description("채널 id"),
                                        fieldWithPath("channels[].name").type(STRING).description("채널명"),
                                        fieldWithPath("channels[].place").type(STRING).description("채널 장소"),
                                        fieldWithPath("channels[].createdBy").type(STRING).description("채널 주인")
                                )
                                .build()
                        )));
    }
}