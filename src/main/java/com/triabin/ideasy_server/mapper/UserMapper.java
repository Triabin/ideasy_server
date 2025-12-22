package com.triabin.ideasy_server.mapper;

import com.triabin.ideasy_server.bean.user.UserDto;
import com.triabin.ideasy_server.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：用户Mapper接口
 * @author Triabin
 * @date 2024-07-12 17:24:36
 */
@Mapper
public interface UserMapper {
    /**
     * 方法描述：查询用户列表
     *
     * @param param 用户查询参数
     * @return {@link List<User>} 用户列表
     * @throws IOException 数据库查询异常
     * @date 2025-11-27 13:26:24
     */
    List<User> selectUsers(UserDto param) throws IOException;

    /**
     * 方法描述：根据id查询用户
     *
     * @param id 用户id
     * @return {@link User}
     * @throws IOException 数据库查询异常
     * @date 2025-11-27 13:27:25
     */
    User getUserById(Integer id) throws IOException;
}
