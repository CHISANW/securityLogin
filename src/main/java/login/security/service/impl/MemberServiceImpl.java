package login.security.service.impl;

import login.security.Entity.LoginType;
import login.security.Entity.Member;
import login.security.dto.MemberDto;
import login.security.repository.MemberRepository;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member join(MemberDto memberDto) {

        Member build = Member.builder()
                .username(memberDto.getUsername())
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .loginType("ORIGINAL").build();
        return memberRepository.save(build);
    }


    @Override
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

}
