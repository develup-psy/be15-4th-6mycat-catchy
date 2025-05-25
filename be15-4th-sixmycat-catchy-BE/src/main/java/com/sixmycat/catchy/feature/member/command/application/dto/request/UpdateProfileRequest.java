package com.sixmycat.catchy.feature.member.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateProfileRequest {
    private String nickname;
    private String statusMessage;
    private String profileImage;
    private List<UpdateCatRequest> cats;
}