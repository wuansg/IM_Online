package xyz.silverspoon.service;

import xyz.silverspoon.bean.ImNotification;

import java.util.List;

public interface ImNotificationService {

    List<ImNotification> list(String uuid);

    ImNotification save(ImNotification notification);
}
