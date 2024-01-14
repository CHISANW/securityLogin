package login.security.controller;

import login.security.Entity.Member;
import login.security.service.EmailService;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailService emailService;
    private final MemberService memberService;

    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id){
        byte[] actualId = Base64.getDecoder().decode(id.getBytes());
        String username = emailService.getUsernameForVerificationId(new String(actualId));
        if(username!=null){
            Member user = memberService.findByLoginId(username);
            user.setVerified(true);
            memberService.save(user);
            return "redirect:/login-emailVerified";
        }
        return "redirect:/login-error";
    }


}
