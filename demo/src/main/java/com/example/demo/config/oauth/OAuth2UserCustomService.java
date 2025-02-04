package com.example.demo.config.oauth;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 리소스 서버에서 보내주는 사용자 정보를 불러오는 loadUser 메소드를 재정의
        // users 테이블에 사용자 정보가 있다면 이름을 업데이트하고 없다면 saveOrUpdate() 메소드로 users 테이블에 회원 데이터를 추가.

        // user 정보를 담은 객체를 반환.
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);

        return user;
    }
}
