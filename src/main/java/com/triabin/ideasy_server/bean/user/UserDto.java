package com.triabin.ideasy_server.bean.user;

import lombok.Getter;

/**
 * 类描述：用户表查询参数
 * @author Triabin
 * @date 2024-07-14 10:36:45
 */
@Getter
public class UserDto {
    /**
     * 性别，MALE：男，FEMALE：女 {@link com.triabin.ideasy_server.common.enums.Gender}
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户名
     */
    private String username;

    public UserDto setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public UserDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }
}
