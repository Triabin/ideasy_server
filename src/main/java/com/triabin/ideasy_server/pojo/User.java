package com.triabin.ideasy_server.pojo;

import com.triabin.ideasy_server.common.enums.Gender;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类描述：平台用户表数据库实体类
 * @author Triabin
 * @date 2024-07-10 08:47:18
 */
@Getter
public class User implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 性别（0：男，1：女）
     * {@link Gender#getValue()}
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime updatedAt;

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
