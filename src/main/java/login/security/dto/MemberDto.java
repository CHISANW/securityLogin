package login.security.dto;

import login.security.Entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class MemberDto {

    private Long id;

    @NotEmpty(message = "아이디를 입력하세요")
    private String username;

    @NotEmpty(message = "아이디를 입력하세요")
    private String loginId;
    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    private String re_password;


    private String email;

    private boolean verified;
}
