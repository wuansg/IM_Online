package xyz.silverspoon.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import xyz.silverspoon.bean.ImUserRelation;

import java.util.List;

@Repository
public class ImUserRelationRepository {
    private final String COLLECTION_NAME = "ImUserRelation";

    @Autowired
    private MongoTemplate template;

    public ImUserRelation save(ImUserRelation relation) {
        return template.insert(relation, COLLECTION_NAME);
    }

    public List<ImUserRelation> listRelations(Query query) {
        return template.find(query, ImUserRelation.class, COLLECTION_NAME);
    }

    public long count(Query query) {
        return template.count(query, ImUserRelation.class, COLLECTION_NAME);
    }

    public UpdateResult delete(Query query) {
        return template.updateFirst(query, Update.update("status", 0), ImUserRelation.class, COLLECTION_NAME);
    }

    public DeleteResult remove(Query query) {
        return template.remove(query, ImUserRelation.class, COLLECTION_NAME);
    }

    public ImUserRelation findOne(Query query) {
        return template.findOne(query, ImUserRelation.class, COLLECTION_NAME);
    }
}
