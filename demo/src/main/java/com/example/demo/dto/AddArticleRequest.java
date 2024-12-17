package com.example.demo.dto;

import com.example.demo.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;

    private String content;

    //DTO는 계층 간 데이터 교환을 위한 객체, Article 엔티티로 변환하는 메소드 정도만 보유. 별도의 비즈니스 로직은 x
    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
