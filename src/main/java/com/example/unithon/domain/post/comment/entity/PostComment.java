package com.example.unithon.domain.post.comment.entity;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_comments")
public class PostComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 500)
    private String content;

    // 답댓글을 위한 자기 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> children = new ArrayList<>();

    @Builder
    private PostComment(Post post, Member member, String content, PostComment parent) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.parent = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    // 답댓글인지 확인
    public boolean isReply() {
        return this.parent != null;
    }

    // 최상위 댓글인지 확인
    public boolean isParentComment() {
        return this.parent == null;
    }
}