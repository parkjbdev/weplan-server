package softwar7.global.argument_resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import softwar7.application.jwt.JwtCreateTokenService;
import softwar7.application.jwt.JwtManager;
import softwar7.domain.jwt.JwtRefreshToken;
import softwar7.domain.member.persist.Member;
import softwar7.domain.member.vo.MemberSession;
import softwar7.domain.member.vo.RoleType;
import softwar7.global.annotation.AdminLogin;
import softwar7.global.exception.ForbiddenException;
import softwar7.global.exception.UnAuthorizedException;
import softwar7.mapper.member.MemberMapper;
import softwar7.repository.jwt.JwtRefreshTokenRepository;
import softwar7.repository.member.MemberRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static softwar7.global.constant.ExceptionMessage.*;
import static softwar7.global.constant.ExceptionMessage.REFRESH_TOKEN_NOT_VALID;
import static softwar7.global.constant.JwtKey.JWT_KEY;
import static softwar7.global.constant.LoginConstant.ACCESS_TOKEN;
import static softwar7.global.constant.LoginConstant.REFRESH_TOKEN;
import static softwar7.global.constant.TimeConstant.*;

@Slf4j
public class AdminLoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final byte[] decodedKey = Base64.getDecoder().decode(JWT_KEY);

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final JwtManager jwtManager;

    public AdminLoginArgumentResolver(final ObjectMapper objectMapper,
                                      final MemberRepository memberRepository,
                                      final JwtRefreshTokenRepository jwtRefreshTokenRepository,
                                      final JwtManager jwtManager) {
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
        this.jwtManager = jwtManager;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean isMemberSessionType = parameter.getParameterType().equals(MemberSession.class);
        boolean isLoginAnnotation = parameter.hasParameterAnnotation(AdminLogin.class);
        return isMemberSessionType && isLoginAnnotation;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)  {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
        String accessToken = request.getHeader(ACCESS_TOKEN.value);
        MemberSession memberSession = getMemberSessionFromToken(accessToken, request, response);
        if (memberSession.roleType() != RoleType.ADMIN) {
            throw new ForbiddenException(ADMIN_FORBIDDEN_EXCEPTION.message);
        }
        return memberSession;
    }

    private MemberSession getMemberSessionFromToken(final String accessToken,
                                                    final HttpServletRequest request,
                                                    final HttpServletResponse response) {
        // AccessToken payload에 MemberSession 객체 정보가 저장되어 있음 -> json 파싱 필요
        try {
            Jws<Claims> claims = getClaims(accessToken);
            String memberSessionJson = claims.getBody().getSubject();
            try {
                return objectMapper.readValue(memberSessionJson, MemberSession.class);
            } catch (JsonProcessingException e) {
                throw new UnAuthorizedException(ACCESS_TOKEN_JSON_PARSING.message);
            }

        } catch (JwtException e) {
            String refreshToken = getRefreshToken(request);
            return getMemberSessionFromRefreshToken(refreshToken, response);
        }
    }

    private Jws<Claims> getClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (IllegalArgumentException e) {
            throw new UnAuthorizedException(CLAIMS_UNAUTHORIZED.message);
        }
    }

    private String getRefreshToken(final HttpServletRequest request) {
        return request.getHeader(REFRESH_TOKEN.value);
    }

    private MemberSession getMemberSessionFromRefreshToken(final String refreshToken,
                                                           final HttpServletResponse response) {
        try {
            Jws<Claims> claims = getClaims(refreshToken);
            String memberId = claims.getBody().getSubject();

            JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.getByMemberId(Long.parseLong(memberId));
            Jws<Claims> expectedClaims = getClaims(jwtRefreshToken.getRefreshToken());
            String expectedMemberId = expectedClaims.getBody().getSubject();

            if (memberId.equals(expectedMemberId)) {
                Member member = memberRepository.getById(Long.parseLong(memberId));
                MemberSession memberSession = MemberMapper.toMemberSession(member);
                String accessToken = jwtManager.createAccessToken(memberSession, ONE_HOUR.value);
                response.setHeader(ACCESS_TOKEN.value, accessToken);

                LocalDateTime lastModifiedAt = jwtRefreshToken.getLastModifiedAt();
                long daysDifference = ChronoUnit.DAYS.between(lastModifiedAt, LocalDateTime.now());

                if (daysDifference >= ONE_DAY.value) {
                    String updateRefreshToken =
                            jwtManager.createRefreshToken(Long.parseLong(memberId), ONE_MONTH.value);
                    jwtManager.saveJwtRefreshToken(Long.parseLong(memberId), updateRefreshToken);
                    response.setHeader(REFRESH_TOKEN.value, updateRefreshToken);
                }

                return memberSession;
            }

            log.info("Client memberId={}, RefreshToken={}", memberId, refreshToken);
            log.info("DB memberId={}, RefreshToken={}", memberId, jwtRefreshToken.getRefreshToken());

            throw new UnAuthorizedException(REFRESH_TOKEN_NOT_MATCH.message);
        } catch (JwtException e) {
            throw new UnAuthorizedException(REFRESH_TOKEN_NOT_VALID.message);
        }
    }
}
