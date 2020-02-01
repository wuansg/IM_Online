package xyz.silverspoon.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.dto.ImRequestDto;
import xyz.silverspoon.service.ImUserRelationRequestService;
import xyz.silverspoon.service.ImUserRelationService;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

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
    private UUIDGenerator uuidGenerator;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<List<ImRequestDto>> getRequests(@PathVariable String uuid) {
        Query query = new Query(Criteria.where("acceptID").is(uuid).and("status").is(0));
        List<ImUserRelationRequest> relationRequests = requestService.getUnfinished(query);
        List<ImRequestDto> requestDtos = new LinkedList<>();
        relationRequests.forEach(o -> {
            ImRequestDto requestDto = new ImRequestDto();
            BeanUtils.copyProperties(o, requestDto);
            requestDto.setRequestUser(userService.getUserByUUID(o.getRequestID()));
            requestDtos.add(requestDto);
        });
        return ImCommonResult.success(requestDtos);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ImCommonResult<String> addFriend(@RequestBody ImUserRelationRequest request) {
        Query query = Query.query(Criteria.where("requestID").is(request.getRequestID()).and("receiverID").is(request.getAcceptID()));
        ImUserRelationRequest already = requestService.getRequest(query);
        if (already != null) {
            return ImCommonResult.error(501, "请求已发送.");
        }
        request.setUUID(uuidGenerator.generateUUID(UUIDType.IM_REQUEST));
        request.setStatus(0);
        request.setCreateTime(new Date());
        request.setModifyTime(request.getCreateTime());
        requestService.addRequest(request);
        return ImCommonResult.success("添加成功.");
    }

    @RequestMapping(value = "/accept/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<String> acceptRequest(@PathVariable String uuid) {
        requestService.accept(uuid);
        ImUserRelationRequest request = requestService.getRequest(Query.query(Criteria.where("UUID").is(uuid)));
        ImUserRelation relation = new ImUserRelation();
        relation.setUUID(uuidGenerator.generateUUID(UUIDType.IM_RELATION));
        relation.setUser1(request.getRequestID());
        relation.setUser2(request.getAcceptID());
        relationService.addRelation(relation);
        //TODO 添加請求同意通知
        return ImCommonResult.success("");
    }

    @RequestMapping(value = "/reject/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<String> rejectRequest(@PathVariable String uuid) {
        requestService.reject(uuid);
        //TODO 添加請求拒絕通知
        return ImCommonResult.success("");
    }
}
