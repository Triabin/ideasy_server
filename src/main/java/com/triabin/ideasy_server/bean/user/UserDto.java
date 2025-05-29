package com.triabin.ideasy_server.bean.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triabin.ideasy_server.common.enums.Gender;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类描述：用户表查询参数
 *
 * @author Triabin
 * @date 2024-07-14 10:36:45
 */
@Getter
public class UserDto {

    /**
     * 用户唯一标识
     */
    private Integer id;

    /**
     * 性别
     *
     * @see Gender
     */
    private List<Gender> genders;

    /**
     * 最小年龄
     */
    private Integer youngerOrEqThan;

    /**
     * 最大年龄
     */
    private Integer olderOrEqThan;

    /**
     * 用户创建最早时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAfterOrEq;

    /**
     * 用户创建最晚时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdBeforeOrEq;

    /**
     * 用户修改最早时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAfterOrEq;

    /**
     * 用户修改最晚时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedBeforeOrEq;

    public UserDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public UserDto setGenders(List<Gender> genders) {
        this.genders = genders;
        return this;
    }

    public UserDto setYoungerOrEqThan(Integer youngerOrEqThan) {
        this.youngerOrEqThan = youngerOrEqThan;
        return this;
    }

    public UserDto setOlderOrEqThan(Integer olderOrEqThan) {
        this.olderOrEqThan = olderOrEqThan;
        return this;
    }

    public UserDto setCreatedAfterOrEq(LocalDateTime createdAfterOrEq) {
        this.createdAfterOrEq = createdAfterOrEq;
        return this;
    }

    public UserDto setCreatedBeforeOrEq(LocalDateTime createdBeforeOrEq) {
        this.createdBeforeOrEq = createdBeforeOrEq;
        return this;
    }

    public UserDto setUpdatedAfterOrEq(LocalDateTime updatedAfterOrEq) {
        this.updatedAfterOrEq = updatedAfterOrEq;
        return this;
    }

    public UserDto setUpdatedBeforeOrEq(LocalDateTime updatedBeforeOrEq) {
        this.updatedBeforeOrEq = updatedBeforeOrEq;
        return this;
    }
}
