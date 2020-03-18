package xyz.silverspoon.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImNotification;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.dto.ImRequestDto;
import xyz.silverspoon.repository.ImNotificationRepository;
import xyz.silverspoon.repository.ImUserRelationRequestRepository;
import xyz.silverspoon.repository.ImUserRepository;
import xyz.silverspoon.service.ImNotificationService;
import xyz.silverspoon.service.ImUserRelationRequestService;
import xyz.silverspoon.service.ImUserService;

import java.util.List;

@Slf4j
@Service
public class ImNotificationServiceImpl implements ImNotificationService {

    @Autowired
    private ImNotificationRepository notificationRepository;
    @Autowired
    private ImUserRelationRequestService requestService;
    @Autowired
    private ImUserService userService;

    @Override
    public List<ImNotification> list(String uuid) {
        Query query = new Query(Criteria.where(Constants.NOTIFICATION_USERID).is(uuid).and(Constants.NOTIFICATION_STATUS).is(Constants.NOTIFICATION_UNREAD));
        List<ImNotification> notifications = notificationRepository.getNotifications(query);
        notifications.forEach(o -> {
            if (o.getType() == Constants.TYPE_REQUEST) {
                ImRequestDto requestDto = new ImRequestDto();
                ImUserRelationRequest request = requestService.getRequestByUUID(o.getContent());
                ImUser user = userService.getUserByUUID(request.getStatus() == 0 ? request.getRequestID() : request.getAcceptID());
                requestDto.setUUID(request.getUUID());
                requestDto.setRequestUser(user);
                requestDto.setStatus(request.getStatus());
                try {
                    o.setContent(new ObjectMapper().writeValueAsString(requestDto));
                } catch (JsonProcessingException e) {
                    log.error("{}", e.getMessage());
                }
            }
        });
        return notifications;
    }

    @Override
    public ImNotification save(ImNotification notification) {
        return notificationRepository.addNotification(notification);
    }
}
