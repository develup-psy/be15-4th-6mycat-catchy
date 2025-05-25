package com.sixmycat.catchy.feature.member.command.domain.repository;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {

    // 특정 사용자 ID로 고양이 목록 조회
    List<Cat> findAllByMember_Id(Long memberId);

    // 생일이 오늘인 고양이들 조회 (뱃지용)
    List<Cat> findAllByBirthDate(java.time.LocalDate birthDay);
}