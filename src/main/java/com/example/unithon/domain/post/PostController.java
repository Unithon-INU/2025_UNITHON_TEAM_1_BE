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

    public ResponseEntity<PostUploadResDto> uploadPost(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PostUploadReqDto uploadRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postService.uploadPost(member, uploadRequest));
    }

    public ResponseEntity<PostGetResDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    public ResponseEntity<List<PostGetResDto>> getPostList(
            @RequestParam(required = false) String sort,      // 정렬: likes(인기순), latest(최신순)
            @RequestParam(required = false) Category category, // 카테고리 필터
            @RequestParam(required = false) String keyword,    // 검색어
            @RequestParam(required = false) Long memberId) {   // 특정 회원 게시글

        // 특정 회원 게시글 조회
        if (memberId != null) {
            return ResponseEntity.ok(postService.getPostsByMember(memberId));
        }

        // 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(postService.searchPosts(keyword));
        }

        // 카테고리 필터
        if (category != null) {
            return ResponseEntity.ok(postService.getPostsByCategory(category));
        }

        // 정렬 방식
        if ("likes".equals(sort)) {
            return ResponseEntity.ok(postService.getTopPostsByLikes());
        }

        // 최신순(기본)
        return ResponseEntity.ok(postService.getPostList());
    }

    public ResponseEntity<List<PostGetResDto>> getTopPostsByLikes() {
        return ResponseEntity.ok(postService.getTopPostsByLikes());
    }

    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @Valid @RequestBody PostUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        postService.updatePost(postId, member, updateRequest);
        return ResponseEntity.ok().build();
    }

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