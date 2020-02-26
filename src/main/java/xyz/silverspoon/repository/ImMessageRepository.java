package xyz.silverspoon.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import xyz.silverspoon.bean.ImMessage;

import java.util.List;

@Repository
public class ImMessageRepository {
    private final String COLLECTION_NAME = "ImMessage";

    @Autowired
    private MongoTemplate template;

    public ImMessage save(ImMessage message) {
        return template.insert(message, COLLECTION_NAME);
    }

    public List<ImMessage> listMessages(Query query) {
        return template.find(query, ImMessage.class, COLLECTION_NAME);
    }

    public UpdateResult update(Query query, Update update) {
        return template.updateMulti(query, update, ImMessage.class, COLLECTION_NAME);
    }
}
