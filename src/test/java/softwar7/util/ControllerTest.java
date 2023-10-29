package softwar7.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import softwar7.application.jwt.JwtFacade;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.repository.channel.ChannelRepository;
import softwar7.repository.member.MemberRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.TimeConstant.ONE_HOUR;
import static softwar7.global.constant.TimeConstant.ONE_MONTH;

@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AcceptanceTest
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ChannelRepository channelRepository;

    @Autowired
    protected JwtFacade jwtFacade;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                        .and()
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080))
                .build();
    }

    protected String login() {
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

        MockHttpServletResponse response = new MockHttpServletResponse();

        // given 3
        String accessToken = jwtFacade.createAccessToken(memberSession, ONE_HOUR.value);
        String refreshToken = jwtFacade.createRefreshToken(memberSession.id(), ONE_MONTH.value);
        jwtFacade.saveJwtRefreshToken(memberSession.id(), refreshToken);
        jwtFacade.setHeader(response, accessToken, refreshToken);

        return response.getHeader(ACCESS_TOKEN.value);
    }
}
