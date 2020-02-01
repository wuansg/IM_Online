package xyz.silverspoon.service.impl;

import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;
import xyz.silverspoon.repository.ImUserRelationRepository;
import xyz.silverspoon.service.ImUserRelationService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.List;

@Service
public class ImUserRelationServiceImpl implements ImUserRelationService {

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ImUserRelationRepository relationRepository;

    @Override
    public List<ImUserRelation> listRelations(ImUser user) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("user1").is(user.getUUID()), Criteria.where("user2").is(user.getUUID()));
        return relationRepository.listRelations(new Query(criteria));
    }

    @Override
    public ImUserRelation addRelation(ImUserRelation relation) {
        relation.setUUID(uuidGenerator.generateUUID(UUIDType.IM_RELATION));
        return relationRepository.save(relation);
    }

    @Override
    public boolean deleteRelation(ImUserRelation relation) {
        Query query = new Query(Criteria.where("UUID").is(relation.getUUID()));
        DeleteResult result = relationRepository.remove(query);
        return result.getDeletedCount() == 1L;
    }
}
