package com.example.demo.dto;

import com.example.demo.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String author;

    private String title;

    private String content;

    // DTO는 계층 간 데이터 교환을 위한 객체, Article 엔티티로 변환하는 메소드 정도만 보유. 별도의 비즈니스 로직은 x
    // 계층 간 데이터 전달을 목적으로, 반환 타입이 Article 인것은 Article 데이터를 계층끼리 전달하겠다는 의도.
    public Article toEntity(String author){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
