package com.example.unithon.domain.post.repository;

import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.post.Category;
import com.example.unithon.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // (전체) 최신순 조회
    List<Post> findAllByOrderByCreatedAtDesc();

    // (카테고리) 최신순 조회
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);

    // 특정 회원 게시글 조회
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 제목 + 내용 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Post> findByTitleOrContent(@Param("keyword") String keyword);
}