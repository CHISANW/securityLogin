package login.security.dto;

import login.security.Entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String loginId;
    private String password;

}
