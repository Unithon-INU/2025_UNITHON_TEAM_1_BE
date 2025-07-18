package com.example.unithon.domain.post.entity;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.enums.Category;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer commentCount = 0;

    // 이미지 URL 필드 추가
    @Column(length = 500)
    private String imageUrl;

    @Builder
    private Post(Member member, Category category, String title, String content, String imageUrl) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        // likeCount, commentCount, createdAt는 자동 설정
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void updatePost(Category category, String title, String content, String imageUrl) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}