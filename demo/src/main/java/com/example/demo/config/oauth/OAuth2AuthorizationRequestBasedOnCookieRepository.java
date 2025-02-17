package com.example.demo.config.oauth;

import com.example.demo.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

/*
    Spring Security의 OAuth2 로그인 과정에서 Authorization Request(인가 요청)을 쿠키(Cookie)에 저장하는 기능을 담당하는 클래스

    OAuth2 로그인 과정에서 Authorization Request를 유지하려면 해당 요청을 어디에 저장해야 하는데, 이 클래스는 이를 쿠키에 저장하는 역할을 함.

    🔹 OAuth2 로그인 과정에서 이 클래스의 역할
    1️⃣ OAuth2 로그인 요청 시, Authorization Request 정보를 쿠키에 저장

    saveAuthorizationRequest() 호출 → 쿠키에 인가 요청 정보 저장
    2️⃣ 로그인 후, Authorization Request를 가져옴

    loadAuthorizationRequest() 호출 → 쿠키에서 인가 요청 정보를 불러옴
    3️⃣ 로그인 완료 후, Authorization Request 관련 쿠키 삭제

    removeAuthorizationRequestCookies() 호출 → 쿠키 삭제
 */
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private final static int COOKIE_EXPIRE_SECONDS = 18000;

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // 클라이언트가 보낸 Http 요청에서 쿠키를 가져와 OAuth2AuthorizationRequest 객체로 변환합니다.
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            // 쿠키를 삭제하교 종료.
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        // OAuth2 로그인 과정에서 인가 요청 정보를 쿠키에 저장하여 로그인 완료 후에도 이 정보를 사용할 수 있도록 한다.
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    // OAuth2 인증 과정이 완료되면 더 이상 Authorization Request가 필요 없어지므로, 쿠키를 삭제.
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}
