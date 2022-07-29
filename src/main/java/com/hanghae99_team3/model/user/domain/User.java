package com.hanghae99_team3.model.user.domain;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.comment.Comment;
import com.hanghae99_team3.model.fridge.Fridge;
import com.hanghae99_team3.model.good.Good;
import com.hanghae99_team3.model.user.domain.dto.UserReqDto;
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
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
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
    public User(String nickname, String password, String email, UserRole role) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String nickname, String password, String email, String userImg, UserRole role, AuthProvider provider, String providerId) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userImg = userImg;
        this.role = role;
        this.authProvider = provider;
        this.providerId = providerId;
    }

    @Builder(builderClassName = "TestRegister", builderMethodName = "testRegister")
    public User(String nickname, String password, String email, String userImg, UserRole role, AuthProvider provider, String providerId, String userDescription) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userImg = userImg;
        this.role = role;
        this.authProvider = provider;
        this.providerId = providerId;
        this.userDescription = userDescription;
    }

    public void update(UserReqDto userDto, String userImg) {
        this.nickname = userDto.getNickname();
        this.userImg = userImg;
        this.userDescription = userDto.getUserDescription();
    }

    public User(@NotNull String nickname) {
        this.nickname = nickname;
        this.role = UserRole.USER;
    }

}
