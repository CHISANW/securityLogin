package login.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {


    @GetMapping("/login")
    public String login(){
        return "login/loginPage";
    }

    @GetMapping("/login-disabled")
    public String loginDisabled(Model model) {
        model.addAttribute("loginDisabled", true);
        return "login/loginPage";
    }

    @GetMapping("/login-emailVerified")
    public String loginVerified(Model model){
        model.addAttribute("loginVerified",true);
        return "login/loginPage";
    }


    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login/loginPage";
    }


}
