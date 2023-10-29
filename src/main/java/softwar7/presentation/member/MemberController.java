package softwar7.presentation.member;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwar7.application.member.MemberSigninService;
import softwar7.application.member.MemberSignupService;
import softwar7.domain.member.persist.Member;
import softwar7.mapper.member.MemberMapper;
import softwar7.mapper.member.dto.MemberResponse;
import softwar7.mapper.member.dto.MemberSigninRequest;
import softwar7.mapper.member.dto.MemberSignupRequest;
import softwar7.mapper.member.dto.SigninResponse;

@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberSignupService memberSignupService;
    private final MemberSigninService memberSigninService;

    public MemberController(final MemberSignupService memberSignupService,
                            final MemberSigninService memberSigninService) {
        this.memberSignupService = memberSignupService;
        this.memberSigninService = memberSigninService;
    }

    @PostMapping("/api/signup")
    public void signup(@RequestBody @Valid final MemberSignupRequest dto) {
        memberSignupService.signup(dto);
    }

    @PostMapping("/api/signin")
    public SigninResponse signin(@RequestBody @Valid final MemberSigninRequest dto,
                                 final HttpServletResponse response) {
        Boolean adminType = memberSigninService.signin(dto, response);
        return new SigninResponse(adminType);
    }
}
