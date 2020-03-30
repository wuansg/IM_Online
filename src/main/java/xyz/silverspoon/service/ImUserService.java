package xyz.silverspoon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.utils.UUIDType;

import java.util.List;

public interface ImUserService {

    ImUser getUserByUsername(String username);

    ImUser getUserByUUID(String UUID);

    UserDetails loadByUsername(String username);

    ImUser addUser(ImUser user);

    Page<ImUser> getUsersLike(String username, int pageNum, int pageSize);

    String updateAvatar(String uuid, MultipartFile file, UUIDType type);

    void update(ImUser user);
}
