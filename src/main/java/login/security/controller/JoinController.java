package login.security.controller;

import login.security.dto.MemberDto;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final MemberService memberService;


    @GetMapping("/join") // 회원가입 폼을 보여줄 URL 지정
    public String showJoinForm(Model model){
        model.addAttribute("member", new MemberDto()); // 빈 MemberDto 객체를 모델에 추가
        return "login/join"; // 보여줄 HTML 뷰의 이름 반환
    }
    @PostMapping("/join")
    public ResponseEntity<?> registerMember(@RequestBody Map<String,Object> MemberInfo){
        try {
            List<String> memberInfo = (List<String>) MemberInfo.get("memberInfo");
            log.info("memberInFO={}",memberInfo);
            memberService.save(memberInfo); // MemberService에서 회원가입 처리 메서드 호출
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }

}
