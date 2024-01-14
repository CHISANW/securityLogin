package login.security.listener;

import login.security.Entity.Member;
import login.security.event.MemberJoinEvent;
import login.security.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<MemberJoinEvent> {

    private final JavaMailSender mailSender;
    private final EmailService emailService;
    @Override
    public void onApplicationEvent(MemberJoinEvent event) {
        Member member = event.getMember();
        String loginId = member.getLoginId();
        String verificationId = emailService.generateVerification(loginId);
        String email = event.getMember().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("회원가입 인증코드 안내");
        message.setText(getText(member,verificationId));
        message.setFrom("wlwhsrjaeka@naver.com");
        message.setTo(email);
        mailSender.send(message);
    }

    private String getText(Member member, String verificationId) {
        String encodedId = new String(Base64.getEncoder().encode(verificationId.getBytes()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(member.getUsername()).append("님, 안녕하세요.").append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("회원가입이 성공적으로 완료되었습니다. ").append(System.lineSeparator());
        buffer.append("계정을 활성화하려면 다음 링크를 클릭하세요: ").append(System.lineSeparator());
        buffer.append("http://localhost:8080/verify/email?id=").append(encodedId);
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("감사합니다.").append(System.lineSeparator()).append("시큐리티 로그인");
        return buffer.toString();
    }
}
