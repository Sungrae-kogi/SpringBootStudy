package com.example.demo.config.jwt;

/*
    토큰을 생성하고, 올바른 토큰인지 유효성 검사와 토큰에서 필요한 정보를 가져오는 클래스
 */

import com.example.demo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        // 현재시간 + 토큰지속시간
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메소드
    private String makeToken(Date expiry, User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ: JWT
                // 내용 iss : whvkek1@gmail.com(yml에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)   // 내용 iat : 현재 시간
                .setExpiration(expiry)  // 내용 exp : expiry 멤버 변숫값
                .setSubject(user.getEmail())    //내용 sub : 유저의 이메일
                .claim("id", user.getId())  // 클레임 id : 유저 id
                /*
                    프론트에서 로그인 시 내려받는 accessToken에서 로그인한 계정의 정보가 부족하다고 요청될 경우
                    필요한 정보들을 Jwts.builder()의 claim()을 추가하여 정보를 담아 전달할 수 있다.
                 */
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecretKey())), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰 유효성 검증 메소드
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            //Jwts.parser()는 최신 버전의 jjwt 라이브러리에서 deprecated 되었습니다, 대신 Jwts.parserBuilder()를 사용합니다.

//            Jwts.parserBuilder()
//                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀키 설정
//                    .build() // JwtParser 생성
//                    .parseClaimsJws(token); // 토큰 파싱 및 검증

            return true;
        }catch(Exception e){    //복호화 과정에서 에러가 나면 유효하지 않은 토큰.
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
