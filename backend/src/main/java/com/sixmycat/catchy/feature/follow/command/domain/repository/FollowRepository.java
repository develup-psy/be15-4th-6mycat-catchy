package com.sixmycat.catchy.feature.follow.command.domain.repository;

import com.sixmycat.catchy.feature.follow.command.domain.aggregate.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

//    boolean existsByFollowerId(Long followerId);
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
//
//    boolean existsByFollowerIdAndFollowingIdAndAcceptedAtIsNullAndRejectedAtIsNull(Long followerId, Long followingId);
//
//    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
//
//    @Query("select count(f) from Follow f where f.followingId = :memberId")
//    long countFollowers(@Param("memberId") Long memberId);
//
//    @Query("select count(f) from Follow f where f.followerId = :memberId")
//    long countFollowings(@Param("memberId") Long memberId);
//
    Optional<Follow> findByFollowerIdAndFollowingId(Long requesterId, Long memberId);

}
