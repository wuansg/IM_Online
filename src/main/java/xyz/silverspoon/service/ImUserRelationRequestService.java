package xyz.silverspoon.service;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import xyz.silverspoon.bean.ImUserRelationRequest;

import java.util.List;

public interface ImUserRelationRequestService {
    //String updateStatus(Query query, Update update);

    void accept(String uuid);

    void reject(String uuid);

    ImUserRelationRequest addRequest(ImUserRelationRequest request);

    List<ImUserRelationRequest> getUnfinished(Query query);

    ImUserRelationRequest getRequest(Query uuid);
}
