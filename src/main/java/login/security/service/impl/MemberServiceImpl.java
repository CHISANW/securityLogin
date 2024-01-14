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
    public Member OAuthGoogleJoin(String loginId, String username) {
        Member googleMember = Member.builder()
                .loginId(loginId)
                .username(username).build();
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) {
            return memberRepository.save(googleMember);
        }
    return member;
    }

    @Override
    public Member OauthFacebookJoin(String loginId, String username) {
        Member faceBookMember = Member.builder()
                .loginId(loginId)
                .username(username).build();
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) {
            return memberRepository.save(faceBookMember);
        }
        return member;
    }

    @Override
    public Member OauthKakaoJoin(String Id, String username) {
        return null;
    }


    @Override
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    private boolean duplicateLoginId(String loginId){
        Member byLoginId = memberRepository.findByLoginId(loginId);
        if (byLoginId.getLoginId().equals(loginId))     //로그인아이디가 포험되어있다면 거짓
            return false;
        else
            return true;                                //없으면 참
    }
}
