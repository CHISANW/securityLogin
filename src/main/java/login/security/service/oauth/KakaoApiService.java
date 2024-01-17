package login.security.service.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Service
@Transactional
@Slf4j
public class KakaoApiService {

    public String kakaoLogout(String accessToken){

        String reqestUrl = "https://kapi.kakao.com/v1/user/logout";

        try{
            HttpURLConnection con = getHttpURLConnection(reqestUrl, "Content-type", "application/x-www-form-urlencoded;charset=utf-8", accessToken);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                StringBuilder response = getStringBuilder(con);

                log.info("카카오 로그아웃 응답: {}", response.toString());
                return "성공";
            } else {
                // 응답이 오류일 때의 처리
                log.error("카카오 로그아웃 실패, 응답 코드: {}", responseCode);
                return "실패";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "실패";
        }
    }

    public String Disconnecting(String accessToken){

        String requestUrl= "https://kapi.kakao.com/v1/user/unlink";
        try{
            HttpURLConnection urlConnection = getHttpURLConnection(requestUrl, "Content-Type", "application/x-www-form-urlencoded", accessToken);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                StringBuilder response = getStringBuilder(urlConnection);

                log.info("카카오 연결 끊기 응답: {}", response.toString());
                return "성공";
            }else {
                log.error("카카오 로그아웃 실패, 응답 코드: {}", responseCode);
                return "실패";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "실패";
        }
    }

    private static HttpURLConnection getHttpURLConnection(String requestUrl, String key, String value, String accessToken) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(key, value);
        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
        return urlConnection;
    }

    private static StringBuilder getStringBuilder(HttpURLConnection con) throws IOException {
        // 응답이 정상적으로 받아졌을 때의 처리
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }
}
