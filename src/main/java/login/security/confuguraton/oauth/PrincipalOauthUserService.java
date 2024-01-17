package login.security.confuguraton.oauth;

import login.security.Entity.Member;
import login.security.confuguraton.oauth.info.FacebookUserInfo;
import login.security.confuguraton.oauth.info.GoogleUserInfo;
import login.security.confuguraton.oauth.info.KakaoUserInfo;
import login.security.confuguraton.oauth.info.Oauth2UserInfo;
import login.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    //후처리 함수
    //구글로 부터 받은 userRequest
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String tokenValue = userRequest.getAccessToken().getTokenValue();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String requestId = userRequest.getClientRegistration().getRegistrationId();
        session.setAttribute("Token",tokenValue);

        Oauth2UserInfo oauth2UserInfo=null;

        if (requestId.equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(), oauth2UserInfo.getName(), requestId);
        } else if (requestId.equals("facebook")) {
            oauth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(), oauth2UserInfo.getName(), requestId);
        } else if (requestId.equals("naver")) {
            Map<String, Object> attribute = oAuth2User.getAttribute("response");
            String id = (String) attribute.get("id");
            String name = (String) attribute.get("name");
            save(id, name, requestId);
        } else if (requestId.equals("kakao")) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            String id = oauth2UserInfo.getId();
            String name = oauth2UserInfo.getName();
            save(id, name, requestId);
        }

        return super.loadUser(userRequest);
    }

    private void save(String id, String name, String clientId) {
        Member build = Member.builder()
                .loginId(id)
                .username(name)
                .loginType(clientId).build();

        Member byLoginId = memberRepository.findByLoginId(id);
        if (byLoginId == null) {
            memberRepository.save(build);
        }
    }

}
