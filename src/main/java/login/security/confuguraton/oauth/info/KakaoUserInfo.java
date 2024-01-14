package login.security.confuguraton.oauth.info;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccountAttributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes=(Map<String,Object>) attributes.get("kakao_account");
    }


    @Override
    public String getName() {
        return kakaoAccountAttributes.get("name").toString();
    }

    @Override
    public String getId() {
        Object idObject = attributes.get("id");
        return (idObject != null) ? idObject.toString() : null;
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }
}
