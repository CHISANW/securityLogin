package login.security.service.impl;

import login.security.Entity.Member;
import login.security.dto.MemberDto;
import login.security.repository.MemberRepository;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void save(List<String> memberInfo) {
        String username = memberInfo.get(0);
        String loginId = memberInfo.get(1);
        String password = memberInfo.get(2);

        Member build = Member.builder()
                .username(username)
                .loginId(loginId)
                .password(passwordEncoder.encode(password)).build();
        memberRepository.save(build);
    }

    @Override
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}
