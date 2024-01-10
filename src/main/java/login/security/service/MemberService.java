package login.security.service;

import login.security.Entity.Member;
import login.security.dto.MemberDto;

import java.util.List;

public interface MemberService {

    void save(List<String> memberInfo);
    Member findByLoginId(String loginId);
}
