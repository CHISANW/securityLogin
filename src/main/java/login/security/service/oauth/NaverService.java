package login.security.service.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String client_Id;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String client_secret;


    public void naverDisconnecting(String accessToken) {
        String requestUrl = "https://nid.naver.com/oauth2.0/token";

        // Set up parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(requestUrl)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", client_Id)
                .queryParam("client_secret", client_secret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER");

        // Create RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                null,
                String.class
        );

        // Handle the response as needed
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Request was successful
            String responseBody = responseEntity.getBody();
            log.info("responsBody={}",responseBody);
        } else {
            log.error("에러발생");
        }
    }
}
