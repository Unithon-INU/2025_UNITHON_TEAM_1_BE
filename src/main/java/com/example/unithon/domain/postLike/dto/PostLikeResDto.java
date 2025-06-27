package com.example.unithon.domain.postLike.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResDto {
    private final boolean isLiked;
    private final int likeCount;

    @Builder
    private PostLikeResDto(boolean isLiked, int likeCount) {
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }

    public static PostLikeResDto of(boolean isLiked, int likeCount) {
        return PostLikeResDto.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }
}