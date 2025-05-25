package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JjureInternalServiceImpl implements JjureInternalService {

    private final JjureRepository jjureRepository;

    @Override
    public Long findMemberIdByJjureId(Long jjureId) {
        return jjureRepository.findMemberIdById(jjureId);
    }
}