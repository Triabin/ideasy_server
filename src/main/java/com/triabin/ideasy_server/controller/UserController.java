package com.triabin.ideasy_server.controller;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.common.dto.Response;
import com.triabin.ideasy_server.pojo.User;
import com.triabin.ideasy_server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：用户Controller层
 * @author Triabin
 * @date 2024-07-12 16:48:54
 */
@RestController("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/queryUsers")
    public Response<List<User>> queryUsers(@RequestBody UserDto params) {
        try {
            return Response.success(userService.selectUsers(params));
        } catch (Exception e) {
            return Response.error();
        }
    }

}
