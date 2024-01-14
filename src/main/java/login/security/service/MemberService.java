package login.security.service;

import login.security.Entity.Member;

import java.util.List;

public interface MemberService {

    void save(Member member);
    Member join(List<String> memberInfo);

    Member OAuthGoogleJoin(String loginId, String username);

    Member OauthFacebookJoin(String loginId, String username);

    Member OauthKakaoJoin(String Id, String username);
    Member findByLoginId(String loginId);
}
