package com.sixmycat.catchy.feature.auth.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtraSignupRequest {
    private String email;
    private String contactNumber;
    private String nickname;
    private String name;
}
