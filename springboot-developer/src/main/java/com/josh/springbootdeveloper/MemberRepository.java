package com.josh.springbootdeveloper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //쿼리를 동적 메소드로 작성하기 - JPA가 정해준 메소드 이름 규칙에 따라 작성한 쿼리 메소드
    Optional<Member> findByName(String name);

    // 복잡한 쿼리이거나 성능 개선을 위해 SQL을 직접 사용하고 싶은 경우, @Query 어노테이션을 사용 가능.
    /*
        @Query("select m from Member m where m.name = ?1")
        Optional<Member> findByNameQuery(String name);
     */
}
