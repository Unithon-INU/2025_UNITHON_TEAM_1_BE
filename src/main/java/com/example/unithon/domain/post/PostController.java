package com.example.unithon.domain.post;

import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.post.dto.req.PostUpdateReqDto;
import com.example.unithon.domain.post.dto.req.PostUploadReqDto;
import com.example.unithon.domain.post.dto.res.PostGetResDto;
import com.example.unithon.domain.post.dto.res.PostUploadResDto;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController implements PostApiDocs {

    private final PostService postService;

    @Override
    public ResponseEntity<PostUploadResDto> uploadPost(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PostUploadReqDto uploadRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postService.uploadPost(member, uploadRequest));
    }

    @Override
    public ResponseEntity<PostGetResDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @Override
    public ResponseEntity<List<PostGetResDto>> getPostList(
            @RequestParam(required = false) String keyword,// 검색 키워드
            @RequestParam(required = false) Category category // 카테고리 필터
            ) {

        // 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(postService.searchPosts(keyword));
        }

        // 카테고리 필터
        if (category != null) {
            return ResponseEntity.ok(postService.getPostsByCategory(category));
        }

        // 기본(최신, 검색어X, 필터X)
        return ResponseEntity.ok(postService.getPostList());
    }

    @Override
    public ResponseEntity<List<PostGetResDto>> getTopPostsByLikes() {
        return ResponseEntity.ok(postService.getTopPostsByLikes());
    }

    @Override
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @Valid @RequestBody PostUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        postService.updatePost(postId, member, updateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId) {
        Member member = getCurrentMember(user);
        postService.deletePost(postId, member);
        return ResponseEntity.noContent().build();
    }

    // 현재 로그인한 사용자의 Member 객체 추출
    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}