package login.security.controller;

import login.security.service.oauth.KakaoApiService;
import login.security.service.oauth.NaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LogoutController {

    private final KakaoApiService kakaoService;
    private final NaverService naverService;

    @PostMapping("/kakaoLogout")
    public ResponseEntity kakaoLogout(HttpSession session){
        try {
            String accessToken = getAccessToken(session);

            String logout = kakaoService.kakaoLogout(accessToken);
            Map<String,String> logoutMap = new HashMap<>();
            
            if (logout.equals("성공")){
                logoutMap.put("success","성공했쪄용");
                return ResponseEntity.ok(logoutMap);
            }
                logoutMap.put("fail",logout);
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(logoutMap);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃중 서버 오류");
        }
    }



    @PostMapping("/kakaoDisconnect")
    public String kakaoDisconnecting(HttpSession session){
        try {
            String accessToken = getAccessToken(session);
            String disconnecting = kakaoService.Disconnecting(accessToken);

            return disconnecting;
        }catch (Exception e){
            e.printStackTrace();
            return "오류";
        }
    }

    @PostMapping("/NaverDisconnect")
    public ResponseEntity<?> naverDisconnecting(HttpSession session){
        try{
            String accessToken = getAccessToken(session);

            naverService.naverDisconnecting(accessToken);
            return ResponseEntity.ok("success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류");
        }
    }

    private static String getAccessToken(HttpSession session) {
        String accessToken = (String) session.getAttribute("Token");
        return accessToken;
    }
    
}
