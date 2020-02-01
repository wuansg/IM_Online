package xyz.silverspoon.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import xyz.silverspoon.bean.ImUserRelation;
import xyz.silverspoon.bean.ImUserRelationRequest;

import java.util.List;

@Repository
public class ImUserRelationRequestRepository {
    private final String COLLECTION_NAME = "ImUserRelationRequest";

    @Autowired
    private MongoTemplate template;

    public List<ImUserRelationRequest> getRequests(Query query) {
        return template.find(query, ImUserRelationRequest.class, COLLECTION_NAME);
    }

    public ImUserRelationRequest getRequest(Query query) {
        return template.findOne(query, ImUserRelationRequest.class, COLLECTION_NAME);
    }

    public ImUserRelationRequest addRequest(ImUserRelationRequest relationRequest) {
        return template.insert(relationRequest, COLLECTION_NAME);
    }

    public UpdateResult updateRequest(Query query, Update update) {
        return template.updateFirst(query, update, ImUserRelationRequest.class, COLLECTION_NAME);
    }
}
