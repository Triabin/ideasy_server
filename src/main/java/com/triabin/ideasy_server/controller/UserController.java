package com.triabin.ideasy_server.controller;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.common.dto.Response;
import com.triabin.ideasy_server.mapper.UserMapper;
import com.triabin.ideasy_server.pojo.User;
import com.triabin.ideasy_server.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

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
    private final UserMapper userMapper;

    @GetMapping("/queryUsers")
    public Response<List<User>> queryUsers(@RequestBody UserDto params) {
        try {
            return Response.success(userService.selectUsers(params));
        } catch (Exception e) {
            logger.error("查询用户信息异常", e);
            return Response.error("查询用户信息异常");
        }
    }

    @GetMapping("/getUser/{userId}")
    public Response<User> getUser(@PathVariable Integer userId) {
        try {
            User user = userMapper.getUserById(userId);
            if (user == null) {
                logger.warn("用户ID为{}的用户不存在", userId);
                return Response.success(String.format("用户ID为%s的用户不存在", userId), null);
            }
            return Response.success(userMapper.getUserById(userId));
        } catch (Exception e) {
            logger.error("查询用户信息异常", e);
            return Response.error("查询用户信息异常");
        }
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody UserDto userDto) {
        try {
            return Response.success();
        } catch (Exception e) {
            logger.error("用户注册异常", e);
            return Response.error("用户注册异常");
        }
    }
}
