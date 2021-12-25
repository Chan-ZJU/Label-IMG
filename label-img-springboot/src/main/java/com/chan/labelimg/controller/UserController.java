package com.chan.labelimg.controller;

import com.chan.labelimg.mapper.UserMapper;
import com.chan.labelimg.pojo.Manager;
import com.chan.labelimg.pojo.Result;
import com.chan.labelimg.pojo.User;
import com.chan.labelimg.service.UserService;
import com.chan.labelimg.validate.Signup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.Objects;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 2:41 PM
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Resource
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录")
    public Result login(HttpServletRequest request, @Validated() @RequestBody User user) {
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);

        User user1 = userService.getByNameAndPassword(username, user.getPassword());
        if (null == user1) {
            return new Result(400, -1);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("userID", user1.getId());
            return new Result(200, user1.getId());
        }
    }

    @CrossOrigin
    @PostMapping("managerLogin")
    public int managerLogin(@RequestBody Manager manager) {
        String username = manager.getUsername();
        username = HtmlUtils.htmlEscape(username);
        String password = manager.getPassword();
        if (userService.getManager(username, password) == null) {
            return 400;
        } else {
            return 200;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/signup")
    @ApiOperation(value = "用户注册")
    public Result signup(HttpServletRequest request, @Validated({Signup.class, Default.class}) @RequestBody User user) {
        HttpSession session = request.getSession();
        session.invalidate();
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        String email = user.getEmail();
        email = HtmlUtils.htmlEscape(email);

        if (userService.isExistEmail(email) || userService.isExistUsername(username)) {
            return new Result(400, -1);
        } else {
            userService.signupUser(username, user.getPassword(), user.getEmail());
            return new Result(200, -1);
        }
    }
}
