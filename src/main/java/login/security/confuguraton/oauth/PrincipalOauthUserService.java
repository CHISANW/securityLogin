package login.security.confuguraton.oauth;

import login.security.Entity.Member;
import login.security.confuguraton.oauth.info.FacebookUserInfo;
import login.security.confuguraton.oauth.info.GoogleUserInfo;
import login.security.confuguraton.oauth.info.KakaoUserInfo;
import login.security.confuguraton.oauth.info.Oauth2UserInfo;
import login.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    //후처리 함수
    //구글로 부터 받은 userRequest
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: "+userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        System.out.println("getRegistrationId: "+userRequest.getClientRegistration().getRegistrationId());
        System.out.println("loadUser: "+super.loadUser(userRequest).getAttributes());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        oAuth2User.getAttribute("id");
        String rqquestId = userRequest.getClientRegistration().getRegistrationId();
        log.info("rqquestId={}",rqquestId);


        Oauth2UserInfo oauth2UserInfo=null;

        if (rqquestId.equals("google")){
            log.info("구글로 로그인 했습니다.");
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),rqquestId);
        }else if(rqquestId.equals("facebook")){
            log.info("페이스북 로그인");
            oauth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),rqquestId);
        }else if (rqquestId.equals("naver")){
            log.info("네이버 로그인");
            Map<String,Object> attribute = oAuth2User.getAttribute("response");
            String id = (String) attribute.get("id");
            String name = (String) attribute.get("name");
            save(id,name,rqquestId);
        }else if (rqquestId.equals("kakao")){
            log.info("카카오톡 로그인");
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            String id = oauth2UserInfo.getId();
            String name = oauth2UserInfo.getName();
            save(id,name,rqquestId);
        }

        return super.loadUser(userRequest);
    }

    private void save(String id, String name,String clientId) {
        Member build = Member.builder()
                .loginId(id)
                .username(name)
                .loginType(clientId).build();

        Member byLoginId = memberRepository.findByLoginId(id);
        if (byLoginId == null){
            memberRepository.save(build);
        }
    }
}
