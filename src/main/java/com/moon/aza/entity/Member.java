package com.moon.aza.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean isValid;

    private String emailToken;

    private LocalDateTime joinedAt;

    private LocalDateTime emailTokenGeneratedAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Board> boards = new ArrayList<>();

    // 이메일 인증 완료 후
    public void verified(){
        this.isValid = true;
        joinedAt = LocalDateTime.now();
    }

    public void generateToken(){
        this.emailToken = UUID.randomUUID().toString();
        this.emailTokenGeneratedAt = LocalDateTime.now();
    }

    // 이메일을 보낸 지 5분이 지났는지 확인
    public boolean enableToSendEmail(){
        return this.emailTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }
    public void changePassword(String password) { this.password = password; }

}
