package com.sixmycat.catchy.feature.block.query.mapper;


import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlockQueryRepository {
    List<BlockResponse> findBlockedUsers(@Param("blockerId") Long blockerId);
}
