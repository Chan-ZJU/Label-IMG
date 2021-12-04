package com.chan.labelimg.controller;

import com.chan.labelimg.mapper.UserMapper;
import com.chan.labelimg.pojo.Result;
import com.chan.labelimg.pojo.User;
import com.chan.labelimg.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 2:41 PM
 */
@Api(tags = "用户登录")
@RestController
@RequestMapping(value = "/api")
public class LoginController {

    @Resource
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录")
    public Result login(@Validated() @RequestBody User user) {
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);

        User user1 = userService.getByNameAndPassword(username, user.getPassword());
        if (null == user1) {
            return new Result(400);
        } else {
            return new Result(200);
        }
    }
}
