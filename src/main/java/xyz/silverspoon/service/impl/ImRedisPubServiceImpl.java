package xyz.silverspoon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import xyz.silverspoon.service.ImRedisPubService;

import javax.annotation.Resource;

@Service
public class ImRedisPubServiceImpl implements ImRedisPubService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean pubMessage(Object message) {
        try {
            redisTemplate.convertAndSend("im_message", message);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
