package com.sixmycat.catchy.feature.jjure.command.domain.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberValidationService {

    private final MemberRepository memberRepository;

    public void validateUploadable(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getDeletedAt() != null) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_DELETED);
        }
    }

    public void validateJjureOwner(Long memberId, Long targetId, ErrorCode errorCode) {
        if (!memberId.equals(targetId)) {
            throw new BusinessException(errorCode);
        }
    }
}
