package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.service.ImUserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class ImUserController {

    @Autowired
    private ImUserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ImCommonResult<Page<ImUser>> search(@RequestParam(required = false) String username,
                                               @RequestParam int pageNum,
                                               @RequestParam int pageSize) {
        Page<ImUser> users = userService.getUsersLike(username, pageNum, pageSize);
        return ImCommonResult.success(users);
    }

    @RequestMapping(value = "/update/avatar/{uuid}", method = RequestMethod.POST)
    public ImCommonResult<String> updateAvatar(@PathVariable String uuid, @RequestBody String avatar) {
        userService.updateAvatar(uuid, avatar);
        return ImCommonResult.success("更新成功.");
    }
}
