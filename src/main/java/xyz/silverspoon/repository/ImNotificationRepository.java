package xyz.silverspoon.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import xyz.silverspoon.bean.ImNotification;

import java.util.List;

@Repository
public class ImNotificationRepository {
    private final String COLLECTION_NAME = "ImNotification";

    @Autowired
    private MongoTemplate template;

    public ImNotification addNotification(ImNotification notification) {
        return template.insert(notification, COLLECTION_NAME);
    }

    public List<ImNotification> getNotifications(Query query) {
        return template.find(query, ImNotification.class, COLLECTION_NAME);
    }

    public UpdateResult updateStatus(Query query, Update update) {
        return template.updateMulti(query, update, COLLECTION_NAME);
    }
}
