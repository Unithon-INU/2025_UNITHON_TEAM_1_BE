package com.example.unithon.domain.post.dto.res;

import com.example.unithon.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUploadResDto {
    private final Long id;
    private final String title;

    @Builder
    private PostUploadResDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static PostUploadResDto from(Post post) {
        return PostUploadResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .build();
    }
}