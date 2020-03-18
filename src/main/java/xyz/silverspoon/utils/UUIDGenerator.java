package xyz.silverspoon.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator {
    @Value(value = "${im_online.server.id: 1}")
    private int serviceId;

    private long systemTime;

    private int count;

    public UUIDGenerator() {
        this.systemTime = System.currentTimeMillis();
        this.count = 0;
    }

    public String generateUUID(UUIDType uuidType) {
        if (systemTime == System.currentTimeMillis()) {
            return String.valueOf(serviceId) + systemTime + uuidType.getType() + count++;
        }
        this.count = 0;
        this.systemTime = System.currentTimeMillis();
        return serviceId + String.valueOf(systemTime) + uuidType.getType() + count++;
    }
}
