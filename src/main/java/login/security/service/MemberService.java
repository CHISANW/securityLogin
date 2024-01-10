package login.security.service;

import login.security.Entity.Member;
import login.security.dto.MemberDto;

public interface MemberService {

    void save(MemberDto memberDto);
}
