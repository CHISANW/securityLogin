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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

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
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberDto memberDto,BindingResult result) {
        try {
            if (result.hasErrors()){
                Map<String,String> errorMessage=new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {

                  errorMessage.put(error.getField(),error.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            Member joinMember = memberService.join(memberDto);
            eventPublisher.publishEvent(new MemberJoinEvent(joinMember));   // 회원 가입시 메일 전송 이벤트 실행
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

}
