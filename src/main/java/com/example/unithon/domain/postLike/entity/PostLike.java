package com.example.unithon.domain.postLike.entity;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "post_id"}))
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    private PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
