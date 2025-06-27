package com.example.unithon.domain.postLike.repository;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.domain.postLike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 특정 회원이 특정 게시글에 좋아요 했는지 확인
    boolean existsByMemberAndPost(Member member, Post post);

    // 특정 회원의 특정 게시글 좋아요 조회
    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    // 특정 게시글의 좋아요 수 조회
    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = :post")
    int countByPost(@Param("post") Post post);

    // 특정 게시글의 모든 좋아요 삭제 (게시글 삭제 시 사용)
    void deleteAllByPost(Post post);
}