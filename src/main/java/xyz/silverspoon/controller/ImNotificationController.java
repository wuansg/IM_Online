package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.silverspoon.bean.ImNotification;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.service.ImNotificationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notification")
public class ImNotificationController {

    @Autowired
    private ImNotificationService notificationService;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<List<ImNotification>> listNotifications(@PathVariable String uuid) {
        List<ImNotification> notifications = notificationService.list(uuid);
        return ImCommonResult.success(notifications);
    }
}
