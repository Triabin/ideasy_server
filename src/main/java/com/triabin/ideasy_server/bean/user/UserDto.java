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
     * 性别，0：男，1：女 {@link com.triabin.ideasy_server.common.enums.Gender}
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户名
     */
    private String userName;

    public UserDto setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public UserDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
