package xyz.silverspoon.controller;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImNotification;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.dto.ImRequestDto;
import xyz.silverspoon.service.*;
import xyz.silverspoon.utils.DateFormatUtils;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/request")
public class ImRequestController {
    @Autowired
    private ImUserRelationRequestService requestService;
    @Autowired
    private ImUserService userService;
    @Autowired
    private ImUserRelationService relationService;
    @Autowired
    private ImRedisPubService redisPubService;
    @Autowired
    private ImNotificationService notificationService;
    @Autowired
    private UUIDGenerator uuidGenerator;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<Page<ImRequestDto>> getRequests(@PathVariable String uuid,
                                                          @RequestParam int pageNum,
                                                          @RequestParam int pageSize) {
        Page<ImUserRelationRequest> relationRequests = requestService.getUnfinished(uuid, pageNum, pageSize);
        List<ImRequestDto> requestDtos = new LinkedList<>();
        relationRequests.forEach(o -> {
            ImRequestDto requestDto = new ImRequestDto();
            BeanUtils.copyProperties(o, requestDto);
            requestDto.setRequestUser(userService.getUserByUUID(o.getRequestID()));
            requestDtos.add(requestDto);
        });
        return ImCommonResult.success(
                PageableExecutionUtils.getPage(requestDtos, PageRequest.of(pageNum, pageSize), relationRequests::getTotalElements)
        );
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ImCommonResult<String> addFriend(@RequestBody ImUserRelationRequest request) throws ParseException {
        ImUserRelation relation = relationService.getRelationByUserID(request.getRequestID(), request.getAcceptID());
        if (relation != null) {
            return ImCommonResult.error(Constants.CODE_EXIST, Constants.IS_FRIEND);
        }
        ImUserRelationRequest already = requestService.getRequestByUserID(request.getRequestID(), request.getAcceptID());
        if (already != null) {
            return ImCommonResult.error(Constants.CODE_EXIST, Constants.DUPLICATE_REQUEST);
        }
        request.setUUID(uuidGenerator.generateUUID(UUIDType.IM_REQUEST));
        requestService.addRequest(request);
        ImNotification notification = new ImNotification();
        notification.setUUID(uuidGenerator.generateUUID(UUIDType.IM_NOTIFICATION));
        notification.setUserID(request.getAcceptID());
        notification.setType(Constants.TYPE_REQUEST);
        notification.setTime(System.currentTimeMillis());
        notification.setStatus(Constants.NOTIFICATION_UNREAD);
        notification.setContent(request.getUUID());
        notificationService.save(notification);
        redisPubService.pubMessage(notification);
        return ImCommonResult.success("添加成功.");
    }

    @RequestMapping(value = "/accept/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<String> acceptRequest(@PathVariable String uuid) {
        UpdateResult updateResult = requestService.accept(uuid);
        if (updateResult.getModifiedCount() > 0) {
            ImUserRelationRequest request = requestService.getRequestByUUID(uuid);
            ImUserRelation relation = new ImUserRelation();
            relation.setUUID(uuidGenerator.generateUUID(UUIDType.IM_RELATION));
            relation.setUser1(request.getRequestID());
            relation.setUser2(request.getAcceptID());
            relationService.addRelation(relation);
            //TODO 添加請求同意通知
            ImNotification notification = new ImNotification();
            notification.setUUID(uuidGenerator.generateUUID(UUIDType.IM_NOTIFICATION));
            notification.setUserID(request.getRequestID());
            notification.setType(Constants.TYPE_REQUEST);
            notification.setStatus(Constants.NOTIFICATION_UNREAD);
            notification.setTime(System.currentTimeMillis());
            notification.setContent(request.getUUID());
            notificationService.save(notification);
            redisPubService.pubMessage(notification);
            return ImCommonResult.success("");
        }
        return ImCommonResult.error(Constants.CODE_EXIST, Constants.DUPLICATE_REQUEST);
    }

    @RequestMapping(value = "/reject/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<String> rejectRequest(@PathVariable String uuid) {
        requestService.reject(uuid);
        ImUserRelationRequest request = requestService.getRequestByUUID(uuid);
        //TODO 添加請求拒絕通知
        ImNotification notification = new ImNotification();
        notification.setUUID(uuidGenerator.generateUUID(UUIDType.IM_NOTIFICATION));
        notification.setUserID(request.getRequestID());
        notification.setType(Constants.TYPE_REQUEST);
        notification.setStatus(Constants.NOTIFICATION_UNREAD);
        notification.setTime(System.currentTimeMillis());
        notification.setContent(request.getUUID());
        notificationService.save(notification);
        redisPubService.pubMessage(notification);
        return ImCommonResult.success("");
    }
}
