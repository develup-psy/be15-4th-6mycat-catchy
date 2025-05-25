package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CatResponse {
    private Long id;
    private String name;
    private String gender;
    private String breed;
    private LocalDate birthDate;
    private Integer age;
}

