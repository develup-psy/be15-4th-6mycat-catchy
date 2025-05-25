package com.sixmycat.catchy.feature.follow.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.follow.command.domain.aggregate.Follow;
import com.sixmycat.catchy.feature.follow.command.domain.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    /* 팔로우 등록 */
    @Override
    public void follow(Long followerId, Long followingId) {

        //자기 자신을 팔로우할 수 없음
        if (followerId.equals(followingId)) {
            throw new BusinessException(ErrorCode.INVALID_FOLLOW);
        }

        // 이미 팔로우 신청한 경우 다시 팔로우 할 수 없음
        boolean alreadyFollowed = followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
        if (alreadyFollowed) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWED);
        }

        //팔로우 요청
        Follow follow = new Follow(followerId, followingId);
        followRepository.save(follow);
    }

    @Override
    public void unfollow(Long followerId, Long followingId) {
        //자기 자신을 팔로우 취소할 수 없음
        if (followerId.equals(followingId)) {
            throw new BusinessException(ErrorCode.INVALID_FOLLOW);
        }

        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLOWED));

        followRepository.delete(follow);
    }
//
    @Override
    public void deleteFollower(Long followingId, Long followerId) {

        //자기 자신을 팔로우 취소할 수 없음
        if (followingId.equals(followerId)) {
            throw new BusinessException(ErrorCode.INVALID_FOLLOW);
        }

        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOW_REQUEST_NOT_FOUND));

        followRepository.delete(follow);
    }
//
//    @Override
//    public void acceptFollowRequest(Long memberId, Long requesterId) {
//        Follow follow = followRepository.findByFollowerIdAndFollowingId(requesterId, memberId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOW_REQUEST_NOT_FOUND));
//
//        //친구 관계인지 확인
//        if (follow.isAccepted()) {
//            throw new BusinessException(ErrorCode.ALREADY_FOLLOWED);
//        }
//
//        //팔로잉 수락
//        follow.accept();
//    }

}

