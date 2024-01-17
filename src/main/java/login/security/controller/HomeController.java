package login.security.controller;

import login.security.Entity.Member;
import login.security.confuguraton.oauth.info.KakaoUserInfo;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal()instanceof OAuth2User){
            OAuth2User user = (OAuth2User) authentication.getPrincipal();

            String name = user.getName();
            Map<String, Object> attributes = user.getAttributes();
            Map<String,Object> attribute = user.getAttribute("response");

            String NaverId="";
            if (attribute!=null) {
               NaverId = (String) attribute.get("id");
            }

            Member member = memberService.findByLoginId(name);
            Member NaverMember = memberService.findByLoginId(NaverId);
            if (member!=null && member.getLoginType().equals("google")) {
                model.addAttribute("GoogleMember", member);
            }else if (member!=null && member.getLoginType().equals("facebook")){
                model.addAttribute("facebookMember",member);
            }
            if (NaverMember != null){
                model.addAttribute("NaverAuth",NaverMember);
            }
            if (attributes != null) {
                KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
                if (kakaoUserInfo != null) {
                    String id = kakaoUserInfo.getId();
                    Member KakoMember = memberService.findByLoginId(id);
                    model.addAttribute("KakaoOauth", KakoMember);
                }
            }
        }

        if(authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            Member byLoginId = memberService.findByLoginId(user.getUsername());
            model.addAttribute("member",byLoginId);
        }
        return "home";
    }
}