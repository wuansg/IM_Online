package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.component.ImUserDetails;
import xyz.silverspoon.service.ImUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//@CrossOrigin(origins = {"http://localhost:8081", "http://localhost"}, allowCredentials = "true")
@RestController
@RequestMapping(value = "/api")
public class ImLoginController {

    @Autowired
    private ImUserService service;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ImCommonResult<ImUser> login(@RequestBody ImUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDetails userDetails = service.loadByUsername(user.getUsername());
        if (userDetails != null) {
            if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("密码错误");
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
            request.getSession().setAttribute("auth", token);
            return ImCommonResult.success(((ImUserDetails)userDetails).getUser());
        }
        return ImCommonResult.error(501, null);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ImCommonResult<ImUser> register(@RequestBody ImUser user) throws Exception {
        ImUser user1 = service.getUserByUsername(user.getUsername());
        if (user1 != null) {
            throw new Exception("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());
        user.setAvatar("");
        user = service.addUser(user);
        return ImCommonResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout() {

    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ImUser home(ImUser user, HttpServletRequest request) {
        System.out.println(user);
        request.getSession().getAttribute("auth");
        return service.getUserByUsername(user.getUsername());
    }
}
