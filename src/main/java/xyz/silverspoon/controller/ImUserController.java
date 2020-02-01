package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.service.ImUserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class ImUserController {

    @Autowired
    private ImUserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ImCommonResult<List<ImUser>> search(@RequestBody(required = false) String username) {
        Query query;
        if (username != null)
            query = new Query(Criteria.where("username").regex(username));
        else
            query = new Query();
        List<ImUser> users = userService.getUsers(query);
        return ImCommonResult.success(users);
    }
}
