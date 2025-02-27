package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // 사용자 이름
    @Column(name = "nickname", unique = true)
    private String nickname;


    @Builder
    public User(String email, String password, String auth, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        //계정 만료 여부 확인
        return true;    // true -> 만료되지 않음.
    }

    @Override
    public boolean isAccountNonLocked() {
        //계정 잠금 여부 확인
        return true;    // true -> 잠금되지 않음.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //패스워드 만료 여부 확인
        return true;    // true -> 만료되지 않음.
    }

    @Override
    public boolean isEnabled() {
        //계정 사용 가능 여부 반환
        return true;    // true -> 사용 가능.
    }

    // 사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;

        return this;
    }
}
