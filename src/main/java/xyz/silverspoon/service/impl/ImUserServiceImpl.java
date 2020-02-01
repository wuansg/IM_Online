package xyz.silverspoon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.component.ImUserDetails;
import xyz.silverspoon.repository.ImUserRepository;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.List;

@Service
public class ImUserServiceImpl implements ImUserService {
    private final ImUserRepository userRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    public ImUserServiceImpl(ImUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ImUser getUserByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return userRepository.getUser(query);
    }

    @Override
    public ImUser getUserByUUID(String UUID) {
        Query query = new Query(Criteria.where("UUID").is(UUID));
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
    public List<ImUser> getUsers(Query query) {
        return userRepository.getUsers(query);
    }

}
