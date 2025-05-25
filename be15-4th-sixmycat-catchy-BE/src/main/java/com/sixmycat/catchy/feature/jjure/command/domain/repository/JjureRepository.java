package com.sixmycat.catchy.feature.jjure.command.domain.repository;

import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JjureRepository extends JpaRepository<Jjure, Long> {

    @Query("SELECT j.memberId FROM Jjure j WHERE j.id = :jjureId")
    Long findMemberIdById(@Param("jjureId") Long jjureId);

    Optional<Jjure> findByMemberId(Long memberId);
}
