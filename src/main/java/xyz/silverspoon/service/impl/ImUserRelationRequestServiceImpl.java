package xyz.silverspoon.service.impl;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.repository.ImUserRelationRequestRepository;
import xyz.silverspoon.service.ImUserRelationRequestService;

import java.util.List;

@Service
public class ImUserRelationRequestServiceImpl implements ImUserRelationRequestService {

    @Autowired
    private ImUserRelationRequestRepository repository;

    public UpdateResult updateStatus(Query query, Update update) {
        return repository.updateRequest(query, update);
    }

    @Override
    public UpdateResult accept(String uuid) {
        Query query = new Query(Criteria.where(Constants.IM_UUID).is(uuid));
        Update update = Update.update(Constants.REQUEST_STATUS, Constants.REQUEST_ACCEPT_STATUS);
        return updateStatus(query, update);
    }

    @Override
    public void reject(String uuid) {
        Query query = new Query(Criteria.where(Constants.IM_UUID).is(uuid));
        Update update = Update.update(Constants.REQUEST_STATUS, Constants.REQUEST_REJECT_STATUS);
        updateStatus(query, update);
    }

    @Override
    public ImUserRelationRequest addRequest(ImUserRelationRequest request) {
        return repository.addRequest(request);
    }

    @Override
    public Page<ImUserRelationRequest> getUnfinished(String uuid, int pageNum, int pageSize) {
        Query query = new Query(Criteria.where(Constants.REQUEST_ACCEPTID).is(uuid).and(Constants.REQUEST_STATUS).is(0));
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        long count = count(query);
        query.with(pageable);
        return PageableExecutionUtils.getPage(repository.getRequests(query), pageable, () -> count);
    }

    @Override
    public ImUserRelationRequest getRequestByUUID(String uuid) {
        Query query = Query.query(Criteria.where(Constants.IM_UUID).is(uuid));
        return repository.getRequest(query);
    }

    @Override
    public ImUserRelationRequest getRequestByUserID(String requestID, String receiveID) {
        Query query = Query.query(Criteria.where(Constants.IM_REQUESTID).in(requestID, receiveID).and(Constants.IM_RECEIVERID).in(receiveID, requestID));
        return repository.getRequest(query);
    }

    @Override
    public long count(Query query) {
        return repository.count(query);
    }
}
