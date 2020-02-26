package xyz.silverspoon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;

import java.util.List;

public interface ImUserService {

    ImUser getUserByUsername(String username);

    ImUser getUserByUUID(String UUID);

    UserDetails loadByUsername(String username);

    ImUser addUser(ImUser user);

    Page<ImUser> getUsersLike(String username, int pageNum, int pageSize);

    void updateAvatar(String uuid, String avatar);
}
