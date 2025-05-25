package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;

public interface TempMemberRedisService {
    void save(TempMember member);
}