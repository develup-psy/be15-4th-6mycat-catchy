package com.sixmycat.catchy.feature.notification.query.mapper;

import com.sixmycat.catchy.feature.notification.query.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationQueryMapper {

    List<NotificationDTO> selectNotifications(Long memberId);

}
