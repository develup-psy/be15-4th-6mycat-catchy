package com.sixmycat.catchy.feature.follow.command.application.service;

public interface
FollowService {
    void follow(Long followerId, Long followingId );
    void unfollow(Long followerId, Long followingId);

    void deleteFollower(Long followingId, Long followerId);
//    void acceptFollowRequest(Long memberId, Long requesterId);
}
