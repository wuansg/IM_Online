package xyz.silverspoon.service;

import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelation;

import java.util.List;

public interface ImUserRelationService {
    List<ImUserRelation> listRelations(ImUser user);

    ImUserRelation addRelation(ImUserRelation relation);

    boolean deleteRelation(ImUserRelation relation);
}
