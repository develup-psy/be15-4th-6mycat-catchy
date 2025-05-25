package com.sixmycat.catchy.feature.member.command.application.dto.request;

import java.time.LocalDate;

public record AddCatRequest(
        String name,
        String gender,
        String breed,
        LocalDate birthDate,
        Integer age
) {}

