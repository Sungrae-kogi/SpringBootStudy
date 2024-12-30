package com.example.demo.controller;

import com.example.demo.dto.AddUserRequest;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request){
        userService.save(request);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        // 로그아웃 api 요청시 로그아웃을 담당하는 핸들러인 SecurityContextLogoutHandler()의 logout()메소드를 호출하여 로그아웃
        /*
            Spring Security는 SecurityContext와 세션을 통해 사용자 인증을 유지.
            이를 안전하게 관리하고 세션을 무효화 하기 위해 SecurityContextLogoutHandler를 사용하는 것이 일반적이고 신뢰할 수 있는 방법.

            로그아웃 시 다음 작업을 수행 : 인증 세션 무효화, SecurityContext 초기화, 쿠키 삭제
            logout() 메소드에 세 번째 인자인 Authentication은 현재 사용자 인증 정보,SecurityContext에서 가져옵니다.
         */
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
    }
}
