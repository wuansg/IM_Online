package xyz.silverspoon.service;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import xyz.silverspoon.bean.ImUserRelationRequest;

import java.util.List;

public interface ImUserRelationRequestService {
    //String updateStatus(Query query, Update update);

    UpdateResult accept(String uuid);

    void reject(String uuid);

    ImUserRelationRequest addRequest(ImUserRelationRequest request);

    Page<ImUserRelationRequest> getUnfinished(String uuid, int pageNum, int pageSize);

    ImUserRelationRequest getRequestByUUID(String uuid);

    ImUserRelationRequest getRequestByUserID(String requestID, String receiveID);

    long count(Query query);
}
