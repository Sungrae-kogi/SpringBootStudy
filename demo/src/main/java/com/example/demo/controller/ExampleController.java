package com.example.demo.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {
    
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){    //Model 객체는 뷰(HTML)로 값을 넘겨주는 객체
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));
        
        // key - value 쌍
        model.addAttribute("person", examplePerson);
        model.addAttribute("today", LocalDate.now());
        
        return "example"; //example.html 이라는 뷰 조회 클래스에 붙은 애너테이션이 @Controller 이므로 뷰의 이름을 반환.
    }

    @Getter
    @Setter
    class Person{
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}

