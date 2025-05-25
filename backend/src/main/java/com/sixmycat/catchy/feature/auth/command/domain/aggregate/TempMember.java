package com.sixmycat.catchy.feature.auth.command.domain.aggregate;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TempMember implements Serializable {
    private String email;
    private String social;
    private String name;
    private String contactNumber;
}