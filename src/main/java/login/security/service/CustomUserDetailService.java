package login.security.service;

import login.security.Entity.Member;
import login.security.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member byLoginId = memberRepository.findByLoginId(username);
        if(byLoginId ==null){
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        return User.builder()
                .username(byLoginId.getLoginId())
                .password(byLoginId.getPassword())
                .disabled(!byLoginId.isVerified())
                .authorities("ROLE_USER").build();
    }

}
