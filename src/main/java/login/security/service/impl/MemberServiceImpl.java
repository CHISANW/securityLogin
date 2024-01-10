package login.security.service.impl;

import login.security.Entity.Member;
import login.security.dto.MemberDto;
import login.security.repository.MemberRepository;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;

    @Override
    public void save(MemberDto memberDto) {
        Member build = Member.builder()
                .username(memberDto.getUsername())
                .loginId(memberDto.getLoginId())
                .password(memberDto.getPassword()).build();
        memberRepository.save(build);
    }
}
