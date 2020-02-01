package xyz.silverspoon.service.impl;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.repository.ImUserRelationRequestRepository;
import xyz.silverspoon.service.ImUserRelationRequestService;

import java.util.List;

@Service
public class ImUserRelationRequestServiceImpl implements ImUserRelationRequestService {

    @Autowired
    private ImUserRelationRequestRepository repository;

    public String updateStatus(Query query, Update update) {
        UpdateResult result = repository.updateRequest(query, update);
        return "";
    }

    @Override
    public void accept(String uuid) {
        Query query = new Query(Criteria.where("UUID").is(uuid));
        Update update = Update.update("status", 1);
        updateStatus(query, update);
    }

    @Override
    public void reject(String uuid) {
        Query query = new Query(Criteria.where("UUID").is(uuid));
        Update update = Update.update("status", 2);
        updateStatus(query, update);
    }

    @Override
    public ImUserRelationRequest addRequest(ImUserRelationRequest request) {
        return repository.addRequest(request);
    }

    @Override
    public List<ImUserRelationRequest> getUnfinished(Query query) {
        return repository.getRequests(query);
    }

    @Override
    public ImUserRelationRequest getRequest(Query uuid) {
        return repository.getRequest(uuid);
    }
}
