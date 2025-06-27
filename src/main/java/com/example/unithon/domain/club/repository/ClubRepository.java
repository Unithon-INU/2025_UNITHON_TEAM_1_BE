package com.example.unithon.domain.club.repository;

import com.example.unithon.domain.club.enums.Division;
import com.example.unithon.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    // (전체) 최신순 조회
    List<Club> findAllByOrderByCreatedAtDesc();

    // (카테고리) 최신순 조회
    List<Club> findByDivisionOrderByCreatedAtDesc(Division division);

    // 동아리 이름 중복 확인
    boolean existsByName(String name);
}