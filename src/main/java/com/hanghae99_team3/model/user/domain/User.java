package com.hanghae99_team3.model.user.domain;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.comment.Comment;
import com.hanghae99_team3.model.fridge.Fridge;
import com.hanghae99_team3.model.good.Good;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

//    @Column(unique = true, nullable = false)
//    private String oAuth2Id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String userImg;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column
    private String providerId;

    @Column
    private String userDescription = "";

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Good> goodList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Fridge> fridgeList = new ArrayList<>();

    public void addBoard(Board board) {
        board.setUser(this);
        this.boardList.add(board);
    }

    public void addComment(Comment comment) {
        comment.setUser(this);
        this.commentList.add(comment);
    }

    public void addGood(Good good) {
        good.setUser(this);
        this.goodList.add(good);
    }

    public void addFridge(Fridge fridge) {
        fridge.setUser(this);
        this.fridgeList.add(fridge);
    }

    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String username, String password, String email, String userImg, UserRole role, AuthProvider provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userImg = userImg;
        this.role = role;
        this.authProvider = provider;
        this.providerId = providerId;
    }

    public User update(String username, String userImg, String userDescription) {
        this.username = username;
        this.userImg = userImg;
        this.userDescription = userDescription;
        return this;
    }

    public User(@NotNull String username) {
        this.username = username;
        this.role = UserRole.USER;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userImg='" + userImg + '\'' +
                ", role=" + role +
                ", authProvider=" + authProvider +
                ", providerId='" + providerId + '\'' +
                '}';
    }


    //
//    /**
//     *UserDetails Methods
//     */
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRole role = this.role;
//        String authority = role.getAuthority();
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
