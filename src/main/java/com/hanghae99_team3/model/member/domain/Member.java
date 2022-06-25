package com.hanghae99_team3.model.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Entity
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

//    @Column(unique = true, nullable = false)
//    private String oAuth2Id;

    @Column(unique = true, nullable = false)
    private String username;

//    @Column
//    private String userImgUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole roles;


    //test 용도
    public Member(String username) {
        this.username = username;
        this.roles = UserRole.USER;
    }

    /**
     *UserDetails Methods
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = this.roles;
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
