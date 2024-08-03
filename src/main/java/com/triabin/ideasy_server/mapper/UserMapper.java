package com.triabin.ideasy_server.mapper;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：
 * @author Triabin
 * @date 2024-07-12 17:24:36
 */
@Mapper
public interface UserMapper {
    List<User> selectUsers(UserDto param) throws IOException;
}
