package com.josh.springbootdeveloper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    /*
        @Sql 어노테이션 : 테스트 실행 전에 SQL 스크립트를 실행시킬 수 있습니다.
     */

    @Sql("/insert-members.sql")
    @Test
    void getAllMembers(){
        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberById(){
        //when
        Member member = memberRepository.findByName("C").get();

        //then
        assertThat(member.getId()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void update(){
        /*
            정상동작하는 이유는 @DataJpaTest 어노테이션이 자동으로 트랜잭션 관리를 설정하기때문.
            따라서 서비스 코드에서 업데이트 기능을 사용하려면, 반드시 서비스 메소드에 @Transactional을 붙여야 함.
            수정 -> 조회 후 트랜잭션 범위 내에서 필드값 변경.
         */

        //given
        Member member = memberRepository.findById(2L).get();

        //when
        member.changeName("BC");

        //then
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("BC");
    }
}
