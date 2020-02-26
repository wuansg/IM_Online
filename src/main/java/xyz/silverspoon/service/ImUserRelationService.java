package xyz.silverspoon.service;

import org.springframework.data.domain.Page;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;

import java.util.List;

public interface ImUserRelationService {
    List<ImUserRelation> listRelations(ImUser user);

    Page<ImUserRelation> listRelationsPage(String uuid, int pageSize, int pageNum);

    ImUserRelation addRelation(ImUserRelation relation);

    boolean deleteRelation(ImUserRelation relation);

    ImUserRelation getRelationByUserID(String requestID, String acceptID);
}
