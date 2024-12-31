package com.example.demo.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 프로퍼티값을 가져와서 사용하는 애너테이션
public class JwtProperties {
    private String issuer;  // yml파일의 jwt.issuer 값이
    private String secretKey;   // yml파일의 jwt.secret_key 값이 매핑됩니다.
}

/*
    YML, 또는 Properties 파일을 Java 객체로 매핑하는 작업을 Spring Boot 에서 제공하는 @ConfigurationProperties 로 처리할 수 있다.
 */
