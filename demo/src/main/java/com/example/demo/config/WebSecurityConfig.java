package com.example.demo.config;

import com.example.demo.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(   // requestMatchers() 특정 요청과 일치하는 url에 대한 엑세스를 설정.
                                "/login",
                                "/signup",
                                "/user"
                        ).permitAll()       //특정 URL은 인증 없이 접근 가능 위에것들
                        .anyRequest().authenticated())  // 그 외의 모든 요청은 인증 필요.
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")    //커스텀 로그인 페이지 설정
                        .defaultSuccessUrl("/articles") // 로그인 성공 시 이동할 URL
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") //로그아웃 성공 시 URL
                        .invalidateHttpSession(true)    //로그아웃을 했으니, 세션을 무효화
                )
                .csrf(AbstractHttpConfigurer::disable)  //csrf 비활성화 CSRF 공격방지를위해 활성화 하는게 좋지만 실습을 편하게 하기 위해 비활성화.
                .build();
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);    //사용자 정보를 가져올 서비스를 설정, 반드시 UserDetailsService를 상속받은 클래스
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); //비밀번호 암호화를 위한 인코더.
        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
