package com.example.unithon.domain.post.service;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.member.repository.MemberRepository;
import com.example.unithon.domain.notification.service.NotificationService;
import com.example.unithon.domain.post.comment.repository.PostCommentRepository;
import com.example.unithon.domain.post.dto.req.PostUpdateReqDto;
import com.example.unithon.domain.post.dto.req.PostUploadReqDto;
import com.example.unithon.domain.post.dto.res.PostGetResDto;
import com.example.unithon.domain.post.dto.res.PostUploadResDto;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.domain.post.enums.Category;
import com.example.unithon.domain.post.repository.PostRepository;
import com.example.unithon.domain.postLike.repository.PostLikeRepository;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostImageService postImageService;
    private final NotificationService notificationService; // 알림 서비스 추가

    // 게시글 업로드 (이미지 포함)
    @Transactional
    public PostUploadResDto uploadPost(Member member, PostUploadReqDto uploadRequest, MultipartFile image) {
        // 이미지 업로드
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = postImageService.uploadImage(image);
        }

        Post post = Post.builder()
                .member(member)
                .category(uploadRequest.getCategory())
                .title(uploadRequest.getTitle())
                .content(uploadRequest.getContent())
                .imageUrl(imageUrl)
                .build();

        postRepository.save(post);
        return PostUploadResDto.from(post);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, Member currentMember, PostUpdateReqDto updateRequest, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        String imageUrl = post.getImageUrl();

        // 새 이미지가 업로드된 경우
        if (image != null && !image.isEmpty()) {
            // 기존 이미지 삭제
            if (imageUrl != null) {
                postImageService.deleteImage(imageUrl);
            }
            // 새 이미지 업로드
            imageUrl = postImageService.uploadImage(image);
        }

        post.updatePost(updateRequest.getCategory(), updateRequest.getTitle(),
                updateRequest.getContent(), imageUrl);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Member currentMember) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        // S3에서 이미지 삭제
        if (post.getImageUrl() != null) {
            postImageService.deleteImage(post.getImageUrl());
        }

        // 게시글과 관련된 알림 삭제
        notificationService.deleteNotificationsByPost(postId);

        // 게시글의 모든 댓글 삭제
        postCommentRepository.deleteAllByPost(post);

        // 게시글 좋아요 삭제
        postLikeRepository.deleteAllByPost(post);

        // 게시글 삭제
        postRepository.delete(post);
    }

    // 개별 게시글 조회
    @Transactional(readOnly = true)
    public PostGetResDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostGetResDto.from(post);
    }

    // 전체 게시글 조회 (최신순)
    @Transactional(readOnly = true)
    public List<PostGetResDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostGetResDto::from)
                .toList();
    }

    // 전체 게시글 조회 (좋아요순 TOP 3)
    @Transactional(readOnly = true)
    public List<PostGetResDto> getTopPostsByLikes() {
        return postRepository.findAllByOrderByLikeCountDescCreatedAtDesc().stream()
                .limit(3)
                .map(PostGetResDto::from)
                .toList();
    }

    // 카테고리별 게시글 조회
    @Transactional(readOnly = true)
    public List<PostGetResDto> getPostsByCategory(Category category) {
        return postRepository.findByCategoryOrderByCreatedAtDesc(category).stream()
                .map(PostGetResDto::from)
                .toList();
    }

    // 특정 회원 게시글 조회
    @Transactional(readOnly = true)
    public List<PostGetResDto> getPostsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        return postRepository.findByMemberOrderByCreatedAtDesc(member).stream()
                .map(PostGetResDto::from)
                .toList();
    }

    // 검색
    @Transactional(readOnly = true)
    public List<PostGetResDto> searchPosts(String keyword) {
        return postRepository.findByTitleOrContent(keyword).stream()
                .map(PostGetResDto::from)
                .toList();
    }
}