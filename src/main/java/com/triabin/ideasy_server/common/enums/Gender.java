package com.triabin.ideasy_server.common.enums;

import lombok.Getter;

/**
 * 类描述：性别枚举类
 * @author Triabin
 * @date 2024-07-12 16:55:21
 */
@Getter
public enum Gender {

    MALE(0, "MALE", "男"),

    FEMALE(1, "FEMALE", "女");

    private final Integer code;

    private final String value;

    private final String desc;

    Gender(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static Gender getInstance(Integer code) {
        for (Gender gender : Gender.values()) {
            if (gender.getCode().equals(code)) return gender;
        }
        return null;
    }
}
