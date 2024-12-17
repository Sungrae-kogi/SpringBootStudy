package com.josh.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    /*
        @AutoConfigureMockMvc -> MockMvc를 생성하고 자동으로 구성하는 애너테이션, 애플리케이션을 서버에 배포하지 않고도 테스트용 MVC 환경을 만들어
        요청 및 전송, 응답 기능을 제공하는 유틸리티 클래스로 컨트롤러를 테스트할 때 사용되는 클래스
     */

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp(){
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers: 아티클 조회에 성공한다.")
    @Test
    void getAllMembers() throws Exception {
        //given
        final String url = "/test";
        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        //when  -> Controller를 테스트 할 때 특정 api 요청을 이런식으로 보낸다, 이로 인해서 실제 서버를 실행하지 않고도 컨트롤러의 동작을 검증할 수 있다.
        // ResultActions 클래스는 perfom()의 반환 값으로, 요청 결과에 대한 조작 및 검증 기능을 제공합니다. ex) 응답 상태코드, 응답 본문, 헤더 값, JSON 데이타 검증
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }


}
