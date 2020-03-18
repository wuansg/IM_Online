package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.service.ImUserRelationRequestService;
import xyz.silverspoon.service.ImUserRelationService;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

//@CrossOrigin(origins = {"http://localhost:8081", "http://localhost"}, allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/friends")
public class ImUserRelationController {

    @Autowired
    private UUIDGenerator uuidGenerator;
    @Autowired
    private ImUserRelationService relationService;
    @Autowired
    private ImUserService userService;
    @Autowired
    private ImUserRelationRequestService requestService;

    @RequestMapping(value = "/{userUUID}", method = RequestMethod.GET)
    public ImCommonResult<Page<ImUser>> getFriends(@PathVariable String userUUID,
                                                   @RequestParam int pageSize,
                                                   @RequestParam int pageNum) {
        ImUser user = new ImUser();
        user.setUUID(userUUID);
        Page<ImUserRelation> relations = relationService.listRelationsPage(userUUID, pageSize, pageNum);
        List<ImUser> friends = new LinkedList<>();
        relations.forEach(relation -> {
            ImUser user1;
            if (!relation.getUser1().equals(userUUID)) {
                user1 = userService.getUserByUUID(relation.getUser1());
            }else {
                user1 = userService.getUserByUUID(relation.getUser2());
            }
            user1.setPassword("");
            friends.add(user1);
        });
        long count = relations.getTotalElements();
        Page<ImUser> users = PageableExecutionUtils.getPage(friends, PageRequest.of(pageNum, pageSize), () -> count);
        return ImCommonResult.success(users);
    }

}
