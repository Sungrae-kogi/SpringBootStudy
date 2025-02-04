package com.example.demo.config;

import com.example.demo.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    /*
    스프링 부트에서 JWT를 사용한 인증 필터를 구현하려면, JwtAuthenticationFilter 클래스를 작성해야 합니다.
    왜냐하면 JWT 토큰을 검증하고, 유효한 토큰인 경우 인증 객체를 생성하여 SecurityContext에 저장해야 하기 때문.
     */
    // access 토큰 값이 담긴 Authorization 헤더 값을 가져온 뒤 액세스 토큰이 유효하다면 인증 정보를 설정.
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //요청 헤더의 Authorization 키 값 조회   "Authorization" : Bearer {JWT} 형식으로 key-value 로 전달되니까.
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);
        //가져온 토큰이 유효한지 확인, 유효한 때에는 인증 정보 설정
        if(tokenProvider.validToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            // ** 중요, ContextHolder는 인증 정보를 저장합니다 거기에 토큰 인증이 완료되었다면 인증 정보를 set
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader){
        // null이 아니고 Bearer 로 시작한다면 Bearer 을 뗀 JWT토큰값을 반환.
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
