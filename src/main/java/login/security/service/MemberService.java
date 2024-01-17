package login.security.service;

import login.security.Entity.Member;
import login.security.dto.MemberDto;

import java.util.List;

public interface MemberService {

    void save(Member member);
    Member join(MemberDto memberDto);

    Member findByLoginId(String loginId);
}
