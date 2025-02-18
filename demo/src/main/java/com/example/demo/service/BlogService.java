package com.example.demo.service;

import com.example.demo.domain.Article;
import com.example.demo.dto.AddArticleRequest;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor    //final 이 붙거나 @NotNull이 붙은 필드의 생성자 추가 -> 이거덕분에 DI가 동작
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메소드
    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(Long id){
        return blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("now found: "+id));
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("now found: "+id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    public void delete(long id){
        blogRepository.deleteById(id);
    }
}
