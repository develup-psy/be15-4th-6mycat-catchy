package com.sixmycat.catchy.feature.member.command.application.service;

import com.sixmycat.catchy.feature.member.command.application.dto.request.AddCatRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest request, MultipartFile imageFile);

    void addCat(Long memberId, AddCatRequest request);

    void deleteCat(Long memberId, Long catId);

}