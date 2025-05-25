package com.sixmycat.catchy.feature.member.query.mapper;

import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberResponse findById(Long id);
    List<Long> findByCatBirth();
}
