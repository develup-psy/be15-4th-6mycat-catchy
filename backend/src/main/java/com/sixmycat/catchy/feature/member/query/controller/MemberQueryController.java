package com.sixmycat.catchy.feature.member.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import com.sixmycat.catchy.feature.member.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        MemberResponse member = memberQueryService.findById(memberId);
        return ApiResponse.success(member);
    }
}
