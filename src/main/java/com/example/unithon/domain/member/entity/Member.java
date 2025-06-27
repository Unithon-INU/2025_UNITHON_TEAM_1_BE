package com.example.unithon.domain.member.entity;

import com.example.unithon.domain.member.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 프로필 사진 URL 필드 추가
    @Column(length = 500)
    private String profileImageUrl;

    @Builder
    private Member(String email, String password, String nickname, Role role, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    // 회원 정보 업데이트 (닉네임과 프로필 사진)
    public void updateMemberInfo(String nickname, String profileImageUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    // 회원 정보 업데이트
    public void updateMemberInfo(String nickname) {
        this.nickname = nickname;
    }

    // 비밀번호 업데이트
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    // 프로필 사진만 업데이트
    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}