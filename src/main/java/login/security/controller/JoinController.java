package login.security.controller;

import login.security.Entity.Member;
import login.security.dto.MemberDto;
import login.security.event.MemberJoinEvent;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/join") // 회원가입 폼을 보여줄 URL 지정
    public String showJoinForm(Model model){
        model.addAttribute("member", new MemberDto()); // 빈 MemberDto 객체를 모델에 추가
        return "login/join"; // 보여줄 HTML 뷰의 이름 반환
    }
    @PostMapping("/join")
    public ResponseEntity<?> registerMember(@RequestBody Map<String,Object> memberInfo) {
        try {
            List<String> memberInfo1 = (List<String>) memberInfo.get("memberInfo");

            if (!memberInfo1.get(3).contains("@")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 형식이 올바르지 않습니다.");
            }
            Member join = memberService.join(memberInfo1);
            eventPublisher.publishEvent(new MemberJoinEvent(join));

            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }

    @PostMapping("/duplicatedId")
    @ResponseBody
    public ResponseEntity<?> duplicatedId(@RequestBody Map<String,Object> duplicateId){
        try {
            String loginId = (String) duplicateId.get("loginId");
            Member byLoginId = memberService.findByLoginId(loginId);

            boolean duplicatedId = true;        //true 중복x , false 중복 o
            if (byLoginId!=null){
                duplicatedId = !(byLoginId.getLoginId().equals(loginId));
            }

            if (!duplicatedId) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 ID입니다.");
            }else if (byLoginId == null && loginId==""){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("빈값으로 요청");
            }
            return ResponseEntity.ok("사용 가능한 ID입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("검사중 오류발생");
        }
    }

    @PostMapping("/duplicatePwd")
    @ResponseBody
    public ResponseEntity<?> duplicatedPwd(@RequestBody Map<String,Object> passwordInfo){
        try {
            String password = (String) passwordInfo.get("password");
            String rePassword = (String) passwordInfo.get("re_password");

            if (password.equals(rePassword)) {
                log.info("Passwords match");
                return ResponseEntity.ok("Passwords match");
            } else {
                log.info("Passwords do not match");
                return ResponseEntity.badRequest().body("Passwords do not match");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀검사 서버 오류");
        }
    }

}
