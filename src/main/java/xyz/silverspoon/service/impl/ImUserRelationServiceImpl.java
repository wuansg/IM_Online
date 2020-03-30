package xyz.silverspoon.service.impl;

import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import xyz.silverspoon.Constants;
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
        criteria.orOperator(Criteria.where(Constants.RELATION_USER1).is(user.getUUID()), Criteria.where(Constants.RELATION_USER2).is(user.getUUID()));
        return relationRepository.listRelations(new Query(criteria));
    }

    @Override
    public List<ImUserRelation> listRelationsPage(String uuid, int pageSize, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where(Constants.RELATION_USER1).is(uuid), Criteria.where(Constants.RELATION_USER2).is(uuid));
        Query query = new Query(criteria).with(pageable);
        return relationRepository.listRelations(query);
    }

    @Override
    public ImUserRelation addRelation(ImUserRelation relation) {
        relation.setUUID(uuidGenerator.generateUUID(UUIDType.IM_RELATION));
        return relationRepository.save(relation);
    }

    @Override
    public boolean deleteRelation(ImUserRelation relation) {
        Query query = new Query(Criteria.where(Constants.IM_UUID).is(relation.getUUID()));
        DeleteResult result = relationRepository.remove(query);
        return result.getDeletedCount() == 1L;
    }

    @Override
    public ImUserRelation getRelationByUserID(String requestID, String acceptID) {
        Query query = new Query(Criteria.where(Constants.RELATION_USER1).in(requestID, acceptID).
                and(Constants.RELATION_USER2).in(requestID, acceptID));
        return relationRepository.findOne(query);
    }
}
