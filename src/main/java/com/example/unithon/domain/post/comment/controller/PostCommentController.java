package com.example.unithon.domain.post.comment.controller;

import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.post.comment.dto.req.PostCommentCreateReqDto;
import com.example.unithon.domain.post.comment.dto.req.PostCommentUpdateReqDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentCreateResDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentResDto;
import com.example.unithon.domain.post.comment.service.PostCommentService;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class PostCommentController implements PostCommentApiDocs {

    private final PostCommentService postCommentService;

    @Override
    public ResponseEntity<PostCommentCreateResDto> createComment(@AuthenticationPrincipal UserDetails user,
                                                                 @PathVariable Long postId,
                                                                 @Valid @RequestBody PostCommentCreateReqDto createRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postCommentService.createComment(postId, member, createRequest));
    }

    @Override
    public ResponseEntity<List<PostCommentResDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postCommentService.getCommentsByPost(postId));
    }

    @Override
    public ResponseEntity<PostCommentResDto> getComment(@PathVariable Long postId,
                                                        @PathVariable Long commentId) {
        return ResponseEntity.ok(postCommentService.getComment(postId, commentId));
    }

    @Override
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @Valid @RequestBody PostCommentUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        postCommentService.updateComment(postId, commentId, member, updateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable Long postId,
                                              @PathVariable Long commentId) {
        Member member = getCurrentMember(user);
        postCommentService.deleteComment(postId, commentId, member);
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