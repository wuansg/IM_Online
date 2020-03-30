package xyz.silverspoon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.component.ImUserDetails;
import xyz.silverspoon.repository.ImUserRepository;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.FileStorageUtils;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.List;

@Service
public class ImUserServiceImpl implements ImUserService {
    private ImUserRepository userRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private FileStorageUtils fileStorageUtils;

    @Autowired
    public ImUserServiceImpl(ImUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ImUser getUserByUsername(String username) {
        Query query = new Query(Criteria.where(Constants.USER_USERNAME).is(username));
        return userRepository.getUser(query);
    }

    @Override
    public ImUser getUserByUUID(String UUID) {
        Query query = new Query(Criteria.where(Constants.IM_UUID).is(UUID));
        return userRepository.getUser(query);
    }

    @Override
    public UserDetails loadByUsername(String username) {
        ImUser user = getUserByUsername(username);
        if (user != null) {
            return new ImUserDetails(user);
        }
        return null;
    }

    @Override
    public ImUser addUser(ImUser user) {
        user.setUUID(uuidGenerator.generateUUID(UUIDType.IM_USER));
        return userRepository.save(user);
    }

    @Override
    public Page<ImUser> getUsersLike(String username, int pageNum, int pageSize) {
        Query query;
        if (username != null)
            query = new Query(Criteria.where(Constants.USER_USERNAME).regex(username));
        else
            query = new Query();
        long count = userRepository.count(query);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        query.with(pageable);
        List<ImUser> users = userRepository.listUsers(query);
        return PageableExecutionUtils.getPage(users, pageable, () -> count);
    }

    @Override
    public String updateAvatar(String uuid, MultipartFile file, UUIDType type) {
        String filepath = fileStorageUtils.saveFile(file, type);
        Query query = new Query(Criteria.where(Constants.IM_UUID).is(uuid));
        Update update = new Update().set(Constants.USER_AVATAR, filepath);
        userRepository.update(query, update);
        return filepath;
    }

    @Override
    public void update(ImUser user) {
        userRepository.save(user);
    }
}
