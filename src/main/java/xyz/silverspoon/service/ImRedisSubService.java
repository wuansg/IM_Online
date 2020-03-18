package xyz.silverspoon.service;

import org.springframework.stereotype.Service;
import xyz.silverspoon.bean.ImMessage;

public interface ImRedisSubService {

    void receiveMessage(String message);
}
