package xyz.silverspoon.service;

import org.springframework.stereotype.Service;

public interface ImRedisPubService {
    boolean pubMessage(Object message);
}
