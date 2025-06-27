package com.example.unithon.domain.post.comment.service;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.comment.dto.req.PostCommentCreateReqDto;
import com.example.unithon.domain.post.comment.dto.req.PostCommentUpdateReqDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentCreateResDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentResDto;
import com.example.unithon.domain.post.comment.entity.PostComment;
import com.example.unithon.domain.post.comment.repository.PostCommentRepository;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.domain.post.repository.PostRepository;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    // 댓글 생성
    @Transactional
    public PostCommentCreateResDto createComment(Long postId, Member member, PostCommentCreateReqDto createRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PostComment parent = null;

        // 답댓글인 경우 부모 댓글 확인
        if (createRequest.getParentId() != 0) {
            parent = postCommentRepository.findById(createRequest.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

            // 부모 댓글이 같은 게시글에 속하는지 확인
            if (!parent.getPost().getId().equals(postId)) {
                throw new CustomException(ErrorCode.INVALID_PARENT_COMMENT);
            }

            // 답댓글의 답댓글은 허용하지 않음 (2depth만 허용)
            if (parent.getParent() != null) {
                throw new CustomException(ErrorCode.INVALID_COMMENT_DEPTH);
            }
        }

        PostComment comment = PostComment.builder()
                .post(post)
                .member(member)
                .content(createRequest.getContent())
                .parent(parent)
                .build();

        postCommentRepository.save(comment);

        // 게시글의 댓글 수 증가
        post.increaseCommentCount();

        return PostCommentCreateResDto.from(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long postId, Long commentId, Member currentMember, PostCommentUpdateReqDto updateRequest) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 게시글 일치 확인
        if (!comment.getPost().getId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        // 작성자 확인
        if (!comment.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        comment.updateContent(updateRequest.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long commentId, Member currentMember) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 게시글 일치 확인
        if (!comment.getPost().getId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        // 작성자 확인
        if (!comment.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        Post post = comment.getPost();

        // 답댓글이 있는 최상위 댓글 삭제 시 답댓글도 함께 삭제
        int deletedCount = 1; // 삭제할 댓글 자체
        if (comment.isParentComment() && !comment.getChildren().isEmpty()) {
            deletedCount += comment.getChildren().size(); // 답댓글들도 포함
        }

        postCommentRepository.delete(comment);

        // 게시글의 댓글 수 감소
        for (int i = 0; i < deletedCount; i++) {
            post.decreaseCommentCount();
        }
    }

    // 게시글의 댓글 목록 조회 (계층구조)
    @Transactional(readOnly = true)
    public List<PostCommentResDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 최상위 댓글들만 조회
        List<PostComment> parentComments = postCommentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post);

        return parentComments.stream()
                .map(PostCommentResDto::from) // 답댓글 포함한 전체 구조
                .collect(Collectors.toList());
    }

    // 개별 댓글 조회
    @Transactional(readOnly = true)
    public PostCommentResDto getComment(Long postId, Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 게시글 일치 확인
        if (!comment.getPost().getId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return PostCommentResDto.from(comment);
    }
}