package xyz.silverspoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.component.ImUserDetails;
import xyz.silverspoon.service.ImUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
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
    public ImCommonResult<ImUser> login(@RequestBody ImUser user,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDetails userDetails = service.loadByUsername(user.getUsername());
        if (userDetails != null) {
            if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException(Constants.BAD_PASSWORDORUSERNAME);
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
            request.getSession().setAttribute("auth", token);
            return ImCommonResult.success(((ImUserDetails)userDetails).getUser());
        }
        throw new Exception(Constants.NOTEXIST_USER);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ImCommonResult<ImUser> register(@RequestBody ImUser user) throws Exception {
        ImUser user1 = service.getUserByUsername(user.getUsername());
        if (user1 != null) {
            throw new Exception(Constants.EXIST_USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(System.currentTimeMillis());
        user.setAvatar("");
        user = service.addUser(user);
        return ImCommonResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute("auth");
    }
}
