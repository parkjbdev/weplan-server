package softwar7.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softwar7.global.argument_resolver.AdminLoginArgumentResolver;
import softwar7.global.argument_resolver.LoginArgumentResolver;
import softwar7.repository.jwt.JwtRefreshTokenRepository;
import softwar7.repository.member.MemberRepository;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public WebConfig(final ObjectMapper objectMapper, final MemberRepository memberRepository,
                     final JwtRefreshTokenRepository jwtRefreshTokenRepository) {
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(objectMapper, memberRepository, jwtRefreshTokenRepository));
        resolvers.add(new AdminLoginArgumentResolver(objectMapper, memberRepository, jwtRefreshTokenRepository));
    }
}
