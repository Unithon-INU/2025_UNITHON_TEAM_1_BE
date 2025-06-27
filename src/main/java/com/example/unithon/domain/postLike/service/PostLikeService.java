package com.example.unithon.domain.postLike.service;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.domain.post.repository.PostRepository;
import com.example.unithon.domain.postLike.dto.PostLikeResDto;
import com.example.unithon.domain.postLike.entity.PostLike;
import com.example.unithon.domain.postLike.repository.PostLikeRepository;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    // 좋아요 토글 (좋아요/좋아요 취소)
    @Transactional
    public PostLikeResDto toggleLike(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 이미 좋아요 했는지 확인
        boolean isLiked = postLikeRepository.existsByMemberAndPost(member, post);

        if (isLiked) {
            // 좋아요 취소
            PostLike postLike = postLikeRepository.findByMemberAndPost(member, post)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

            postLikeRepository.delete(postLike);
            post.decreaseLikeCount();

            return PostLikeResDto.of(false, post.getLikeCount());
        } else {
            // 좋아요 추가
            PostLike postLike = PostLike.builder()
                    .member(member)
                    .post(post)
                    .build();

            postLikeRepository.save(postLike);
            post.increaseLikeCount();

            return PostLikeResDto.of(true, post.getLikeCount());
        }
    }

    // 좋아요 상태 조회
    @Transactional(readOnly = true)
    public PostLikeResDto getLikeStatus(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        boolean isLiked = postLikeRepository.existsByMemberAndPost(member, post);
        return PostLikeResDto.of(isLiked, post.getLikeCount());
    }
}