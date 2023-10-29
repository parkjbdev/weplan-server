package softwar7.presentation.member;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.RoleType;
import softwar7.mapper.member.dto.MemberSigninRequest;
import softwar7.mapper.member.dto.MemberSignupRequest;
import softwar7.util.ControllerTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("게스트 회원가입 성공")
    void guestSignupSuccess() throws Exception {
        // given
        MemberSignupRequest dto = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .name("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.GUEST)
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("게스트 회원 가입 성공",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("회원가입")
                                .requestFields(
                                        fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(STRING).description("비밀번호"),
                                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("roleType").type(STRING).description("권한")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("관리자 회원가입 성공")
    void adminSignupSuccess() throws Exception {
        // given
        MemberSignupRequest dto = MemberSignupRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .name("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .adminPassword("관리자 비밀번호 1234")
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("관리자 회원 가입 성공",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("회원가입")
                                .requestFields(
                                        fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(STRING).description("비밀번호"),
                                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("roleType").type(STRING).description("권한"),
                                        fieldWithPath("adminPassword").type(STRING).description("관리자 비밀번호")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("회원가입 실패")
    void signupFail() throws Exception {
        // given
        MemberSignupRequest dto = MemberSignupRequest.builder()
                .loginId("로그인 아이디 형식을 지키지 않음 (6자 ~ 15자)")
                .password("비밀번호 1234 형식을 지키지 않음 (8자 ~ 15자)")
                .name("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.GUEST)
                .build();

        // expected
        mockMvc.perform(post("/api/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andDo(document("회원가입 실패",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("회원가입")
                                .requestFields(
                                        fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(STRING).description("비밀번호"),
                                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("roleType").type(STRING).description("권한")
                                )
                                .responseFields(
                                        fieldWithPath("statusCode").type(STRING).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("예외 메세지")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("로그인 성공")
    void signinSuccess() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        MemberSigninRequest dto = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234")
                .build();

        // expected
        mockMvc.perform(post("/api/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(document("로그인 성공",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("로그인")
                                .requestFields(
                                        fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(STRING).description("비밀번호")
                                )
                                .responseFields(
                                        fieldWithPath("isAdmin").type(BOOLEAN).description("관리자 여부")
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("로그인 실패")
    void signinFail() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("비밀번호 1234");

        Member member = Member.builder()
                .loginId("로그인 아이디")
                .password(encodedPassword)
                .username("사용자 이름")
                .phoneNumber("01012345678")
                .roleType(RoleType.ADMIN)
                .build();

        memberRepository.save(member);

        MemberSigninRequest dto = MemberSigninRequest.builder()
                .loginId("로그인 아이디")
                .password("비밀번호 1234 틀림")
                .build();

        // expected
        mockMvc.perform(post("/api/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andDo(document("로그인 실패",
                        preprocessRequest(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("로그인")
                                .requestFields(
                                        fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(STRING).description("비밀번호")
                                )
                                .responseFields(
                                        fieldWithPath("statusCode").type(STRING).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("예외 메세지")
                                )
                                .build()
                        )));
    }

//    @Test
//    @DisplayName("닉네임 검증을 했을 때 사용가능하면 true를 반환합니다")
//    void validateNicknameDuplicationSuccess() throws Exception {
//        // given
//        MemberSignupRequest dto = MemberSignupRequest.builder()
//                .loginId("로그인 아이디")
//                .password("비밀번호 1234")
//                .name("사용자 이름")
//                .phoneNumber("01012345678")
//                .roleType(RoleType.GUEST)
//                .build();
//
//        // expected
//        mockMvc.perform(post("/api/nickname-validation")
//                        .header(ACCESS_TOKEN.value, accessToken)
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto))
//                )
//                .andExpect(status().isOk())
//                .andDo(document("닉네임 검증 - 사용 가능",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        resource(ResourceSnippetParameters.builder()
//                                .tag("회원")
//                                .summary("닉네임 검증")
//                                .requestHeaders(
//                                        headerWithName(ACCESS_TOKEN.value).description("AccessToken")
//                                )
//                                .requestFields(
//                                        fieldWithPath("nickname").type(STRING).description("닉네임")
//                                )
//                                .responseFields(
//                                        fieldWithPath("statusCode").type(STRING).description("상태 코드"),
//                                        fieldWithPath("message").type(STRING).description("사용 여부 메세지")
//                                )
//                                .build()
//                        )));
//    }
}