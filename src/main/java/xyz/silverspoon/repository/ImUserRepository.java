package xyz.silverspoon.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;

import java.util.List;

@Repository
public class ImUserRepository {
    private final String COLLECTION_NAME = "ImUser";

    @Autowired
    private MongoTemplate template;

    public ImUser save(ImUser user) {
        return template.insert(user, COLLECTION_NAME);
    }

    public ImUser getUser(Query query) {
        return template.findOne(query, ImUser.class, COLLECTION_NAME);
    }

    public List<ImUser> listUsers(Query query) {
        return template.find(query, ImUser.class, COLLECTION_NAME);
    }

    public long count(Query query) {
        return template.count(query, ImUser.class, COLLECTION_NAME);
    }

    public UpdateResult update(Query query, Update update) {
        return template.updateFirst(query, update, ImUser.class, COLLECTION_NAME);
    }
}
