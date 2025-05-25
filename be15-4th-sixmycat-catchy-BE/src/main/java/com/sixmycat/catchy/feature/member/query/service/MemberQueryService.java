package com.sixmycat.catchy.feature.member.query.service;

import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import com.sixmycat.catchy.feature.member.query.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberMapper memberMapper;

    public MemberResponse findById(Long id) {
        return memberMapper.findById(id);
    }
}
