package com.sixmycat.catchy.feature.jjure.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;
import com.sixmycat.catchy.feature.jjure.query.mapper.JjureQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JjureCommentQueryServiceImpl implements JjureCommentQueryService {

    private final JjureQueryMapper jjureQueryMapper;

    @Override
    public PageResponse<JjureCommentResponse> getComments(Long jjureId, int page, int size) {
        if (!jjureQueryMapper.existsByJjureId(jjureId)) {
            throw new BusinessException(ErrorCode.JJURE_NOT_FOUND);
        }

        PageHelper.startPage(page, size);
        PageInfo<JjureCommentResponse> pageInfo =
                new PageInfo<>(jjureQueryMapper.findCommentsByJjureId(jjureId));

        return PageResponse.from(pageInfo);
    }
}
