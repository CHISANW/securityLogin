package login.security.Entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


@Data
@Slf4j
public class KakaoApi {

//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String apikey;

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakapRedirectUri;

    private String AccessToken;

    public String getAccessToken(String code){
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=utf-8");
            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(apikey);
            sb.append("&redirect_uri=").append(kakapRedirectUri);
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = con.getResponseCode();
            log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);

            BufferedReader br;

            if(responseCode>=200&&responseCode<=300){
                br =new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else {
                br= new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
                log.info("lien={}",line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken){
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        log.info("accessToken={}",accessToken);

        HashMap<String ,Object> userInfo = new HashMap<>();
        try {
            URL url = new URL(reqUrl);
            userInfo.put("acctoKen", accessToken);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer "+accessToken);
            urlConnection.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = urlConnection.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            String line ="";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null){
                stringBuilder.append(line);
            }
            String result = stringBuilder.toString();
            log.info("responseBody={}",result);

            JsonParser parser  = new JsonParser();
            JsonElement element  = parser .parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String id = element.getAsJsonObject().get("id").getAsString();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String username = kakaoAccount.getAsJsonObject().get("name").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickName",nickname);
            userInfo.put("username",username);
            userInfo.put("email",email);
            userInfo.put("id",id);


            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }
}
