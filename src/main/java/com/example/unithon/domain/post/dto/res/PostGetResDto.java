package com.example.unithon.domain.post.dto.res;

import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.domain.post.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostGetResDto {
    private final Long id;
    private final String nickname;
    private final Category category;
    private final String title;
    private final String content;
    private final String imageUrl;
    private final Integer likeCount;
    private final Integer commentCount;
    private final LocalDateTime createdAt;

    @Builder
    private PostGetResDto(Long id, String nickname, Category category,
                          String title, String content, String imageUrl,
                          Integer likeCount, Integer commentCount, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    public static PostGetResDto from(Post post) {
        return PostGetResDto.builder()
                .id(post.getId())
                .nickname(post.getMember().getNickname())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}