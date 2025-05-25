package com.sixmycat.catchy.feature.member.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UpdateCatRequest {
    private Long id;
    private String name;
    private String gender;
    private String breed;
    private LocalDate birthDate;
    private Integer age;
}