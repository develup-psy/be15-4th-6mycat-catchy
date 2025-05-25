package com.sixmycat.catchy.feature.member.command.application.service;

import com.sixmycat.catchy.common.s3.S3Uploader;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.member.command.application.dto.request.AddCatRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.feature.member.query.dto.response.CatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest request, MultipartFile imageFile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String imageUrl = member.getProfileImage(); // 기본 유지
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Uploader.uploadFile(imageFile, "profile").url();
        }

        member.updateProfile(
                request.getNickname(),
                request.getStatusMessage(),
                imageUrl
        );

        List<CatResponse> catResponses = null;

        if (request.getCats() != null) {
            catResponses = request.getCats().stream()
                    .filter(catReq -> catReq.getId() != null) // ✅ 기존 고양이만 처리
                    .map(catReq -> {
                        Cat cat = member.getCats().stream()
                                .filter(c -> c.getId().equals(catReq.getId()))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException(ErrorCode.CAT_NOT_FOUND));

                        cat.updateCatInfo(
                                catReq.getName(),
                                catReq.getGender(),
                                catReq.getBreed(),
                                catReq.getBirthDate(),
                                catReq.getAge()
                        );

                        return new CatResponse(
                                cat.getId(),
                                cat.getName(),
                                cat.getGender(),
                                cat.getBreed(),
                                cat.getBirthDate(),
                                cat.getAge()
                        );
                    })
                    .toList();
        }

        return new UpdateProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getStatusMessage(),
                member.getProfileImage(),
                catResponses
        );
    }

    @Transactional
    public void addCat(Long memberId, AddCatRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Cat cat = new Cat(
                request.name(),
                request.gender(),
                request.breed(),
                request.birthDate(),
                request.age(),
                member
        );

        member.addCat(cat); // 연관관계 설정
    }

    @Override
    @Transactional
    public void deleteCat(Long memberId, Long catId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Cat cat = member.getCats().stream()
                .filter(c -> c.getId().equals(catId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.CAT_NOT_FOUND));

        member.getCats().remove(cat); // List에서 제거

        // 선택: 연관관계 정리
        cat.delete(); // 만약 soft delete라면 이 메서드를 Cat 엔티티에 정의
    }
}
