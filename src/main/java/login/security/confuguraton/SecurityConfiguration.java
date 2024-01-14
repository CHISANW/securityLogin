package login.security.confuguraton;

import login.security.confuguraton.oauth.PrincipalOauthUserService;
import login.security.handler.CustomAuthenticationFailureHandler;
import login.security.handler.OAuth2AuthorizationSuccessHandler;
import login.security.service.CustomUserDetailService;
import login.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final OAuth2AuthorizationSuccessHandler oAuth2AuthorizationSuccessHandler;
    private final PrincipalOauthUserService customOAuth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
                .antMatchers("/login","/","/join","/login-disabled","/login-error","/login-emailVerified","/verify/email","/duplicatedId").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler) .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .rememberMe().key("remember-me-key").rememberMeCookieName("security-remember-me")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(new OAuth2AuthorizationSuccessHandler())
                        .userInfoEndpoint().
                userService(customOAuth2UserService);

        http.cors()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/img/**","/css/**","/js/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailService();
    }
}
