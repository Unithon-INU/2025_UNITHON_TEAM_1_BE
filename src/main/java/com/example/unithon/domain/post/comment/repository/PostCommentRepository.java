package com.example.unithon.domain.post.comment.repository;

import com.example.unithon.domain.post.comment.entity.PostComment;
import com.example.unithon.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    // 특정 게시글의 최상위 댓글들만 조회 (답댓글 제외)
    List<PostComment> findByPostAndParentIsNullOrderByCreatedAtAsc(Post post);

    // 특정 게시글의 모든 댓글 조회 (최상위 댓글 + 답댓글)
    @Query("SELECT c FROM PostComment c WHERE c.post = :post ORDER BY " +
            "CASE WHEN c.parent IS NULL THEN c.id ELSE c.parent.id END ASC, " +
            "c.parent.id ASC, c.createdAt ASC")
    List<PostComment> findAllByPostOrderByHierarchy(@Param("post") Post post);

    // 특정 댓글의 답댓글들 조회
    List<PostComment> findByParentOrderByCreatedAtAsc(PostComment parent);

    // 특정 게시글의 전체 댓글 수 (답댓글 포함)
    int countByPost(Post post);

    // 특정 게시글의 모든 댓글 삭제 (게시글 삭제 시 사용)
    void deleteAllByPost(Post post);
}