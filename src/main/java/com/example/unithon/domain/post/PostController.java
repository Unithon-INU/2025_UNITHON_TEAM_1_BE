package com.example.unithon.domain.post;

import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.post.dto.req.PostUpdateReqDto;
import com.example.unithon.domain.post.dto.req.PostUploadReqDto;
import com.example.unithon.domain.post.dto.res.PostGetResDto;
import com.example.unithon.domain.post.dto.res.PostUploadResDto;
import com.example.unithon.domain.post.service.PostService;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController implements PostApiDocs {

    private final PostService postService;

    // 게시글 작성(이미지 첨부 가능) - RequestParam으로 개별 필드 받기
    @Override
    public ResponseEntity<PostUploadResDto> uploadPost(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("category") Category category,// required=true (기본값)
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        // DTO 생성
        PostUploadReqDto uploadRequest = new PostUploadReqDto(category, title, content);

        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postService.uploadPost(member, uploadRequest, image));
    }

    //개별 게시글 조회
    @Override
    public ResponseEntity<PostGetResDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    //게시글 목록 조회
    @Override
    public ResponseEntity<List<PostGetResDto>> getPostList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category) {

        //키워드 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(postService.searchPosts(keyword));
        }

        //카테고리 필터링
        if (category != null) {
            return ResponseEntity.ok(postService.getPostsByCategory(category));
        }

        //전체 조회(최신순)
        return ResponseEntity.ok(postService.getPostList());
    }

    //인기 게시글 Top 3 조회(좋아요순)
    @Override
    public ResponseEntity<List<PostGetResDto>> getTopPostsByLikes() {
        return ResponseEntity.ok(postService.getTopPostsByLikes());
    }

    //게시글 수정(본인만 가능) - RequestParam으로 개별 필드 받기
    @Override
    public ResponseEntity<Void> updatePost(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long postId,
            @RequestParam("category") Category category,// required=true (기본값)
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        // DTO 생성
        PostUpdateReqDto updateRequest = new PostUpdateReqDto(category, title, content);

        Member member = getCurrentMember(user);
        postService.updatePost(postId, member, updateRequest, image);
        return ResponseEntity.ok().build();
    }

    //게시글 삭제(본인만 가능)
    @Override
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId) {
        Member member = getCurrentMember(user);
        postService.deletePost(postId, member);
        return ResponseEntity.noContent().build();
    }

    //현재 로그인한 사용자 정보 추출
    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}