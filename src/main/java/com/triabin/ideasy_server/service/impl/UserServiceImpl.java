package com.triabin.ideasy_server.service.impl;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.mapper.UserMapper;
import com.triabin.ideasy_server.pojo.User;
import com.triabin.ideasy_server.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：用户表service层接口实现类
 * @author Triabin
 * @date 2024-07-12 17:26:36
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private UserMapper userMapper;

    @Override
    public List<User> selectUsers(UserDto param) throws IOException {
        return userMapper.selectUsers(param);
    }
}
