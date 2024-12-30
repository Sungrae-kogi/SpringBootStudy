package com.example.demo.controller;

import com.example.demo.domain.Article;
import com.example.demo.dto.ArticleListViewResponse;
import com.example.demo.dto.ArticleViewResponse;
import com.example.demo.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();  // 이 부분 캠프에서 많이 했다, Article 리스트를 받아와서 생성자 호출로 ArticleListViewResponse 객체로 변환.

        model.addAttribute("articles", articles);   // 블로그 글 리스트를 Model에 담는다.

        return "articleList";
    }

    /*
        수정과, 생성을 한 화면에서 처리하려고 함, Param이 없다면 생성이고 있다면 수정, 따라서 required = false 설정
     */
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            // id가 전달되지 않았다면, 빈 내용의 View를 전달하여 생성할 Article의 내용을 입력하도록.
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            // id가 전달되었다면, 그 내용을 가져와서 View에 띄우고 수정을 할 수 있도록 해야 함.
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        //@Controller 와 method의 리턴 타입이 String -> resources/template에서 String.html 파일을 전달한다.
        return "newArticle";
    }
}
