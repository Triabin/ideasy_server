package com.triabin.ideasy_server.controller;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.common.dto.Response;
import com.triabin.ideasy_server.pojo.User;
import com.triabin.ideasy_server.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：用户Controller层
 * @author Triabin
 * @date 2024-07-12 16:48:54
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final IUserService userService;

    @GetMapping("/queryUsers")
    public Response<List<User>> queryUsers(@RequestBody UserDto params) {
        try {
            return Response.success(userService.selectUsers(params));
        } catch (Exception e) {
            logger.error("查询用户信息异常", e);
            return Response.error("查询用户信息异常");
        }
    }
}
