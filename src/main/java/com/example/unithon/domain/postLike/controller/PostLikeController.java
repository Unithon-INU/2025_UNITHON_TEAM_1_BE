package com.example.unithon.domain.postLike.controller;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.postLike.service.PostLikeService;
import com.example.unithon.domain.postLike.dto.PostLikeResDto;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController implements PostLikeApiDocs {

    private final PostLikeService postLikeService;

    @Override
    public ResponseEntity<PostLikeResDto> toggleLike(@AuthenticationPrincipal UserDetails user,
                                                     @PathVariable Long postId) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postLikeService.toggleLike(postId, member));
    }

    @Override
    public ResponseEntity<PostLikeResDto> getLikeStatus(@AuthenticationPrincipal UserDetails user,
                                                        @PathVariable Long postId) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(postLikeService.getLikeStatus(postId, member));
    }

    // 현재 로그인한 사용자의 Member 객체 추출
    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}