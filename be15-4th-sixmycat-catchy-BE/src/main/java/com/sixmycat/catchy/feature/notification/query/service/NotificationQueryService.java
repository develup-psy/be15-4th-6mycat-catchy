package com.sixmycat.catchy.feature.notification.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.notification.query.dto.NotificationDTO;
import com.sixmycat.catchy.feature.notification.query.mapper.NotificationQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final NotificationQueryMapper notificationMapper;

    /* 알림 목록 조회 */
    @Transactional(readOnly = true)
    public PageResponse<NotificationDTO> getNotifications(Long memberId, int page, int size) {

        PageHelper.startPage(page, size);
        PageInfo<NotificationDTO> pageInfo = new PageInfo<>(notificationMapper.selectNotifications(memberId));

        return PageResponse.from(pageInfo);

    }
}
