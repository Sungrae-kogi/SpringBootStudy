package com.example.demo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {
    //요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);   //만료 기간?
        // HttpServletResponse 객체에 쿠키를 담아서 응답하라
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        // 쿠키를 삭제하는 코드
        /*
            쿠키를 삭제하는 원리는 "브라우저가 쿠키를 무효화 하도록 유도하는것"
            쿠키는 클라이언트의 "브라우저"에 저장되므로 서버에서 직접 삭제할 수 없고
            서버는 클라이언트로부터 전달받은 쿠키를 무효화하는 방식으로 브라우저가 삭제하도록 유도를 할 수 있다. ex) setMaxAge(0)
         */
        for(Cookie cookie : cookies){
            if(name.equals(cookie.getName())){
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object obj){
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역 직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls){
        return cls.cast(
                SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue()))
        );
    }
}
