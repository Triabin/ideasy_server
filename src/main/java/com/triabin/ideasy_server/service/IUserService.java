package com.triabin.ideasy_server.service;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.pojo.User;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：
 * @author Triabin
 * @date 2024-07-12 16:49:56
 */
@Repository
public interface IUserService {

    /**
     * 方法描述：条件查询用户
     * @param param 查询参数
     * @return {@link List}
     * @throws IOException
     * @date 2024-07-14 10:44:53
     */
    List<User> selectUsers(UserDto param) throws IOException;
}
