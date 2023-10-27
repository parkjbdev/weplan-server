package softwar7.presentation.member;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwar7.application.member.MemberSignupService;
import softwar7.mapper.member.dto.MemberSignupRequest;

@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberSignupService memberSignupService;

    public MemberController(final MemberSignupService memberSignupService) {
        this.memberSignupService = memberSignupService;
    }

    @PostMapping("/api/signup")
    public void signup(@RequestBody final MemberSignupRequest dto) {
        memberSignupService.signup(dto);
    }
}
