package com.triabin.ideasy_server.common.enums;

/**
 * 类描述：性别枚举类
 * @author Triabin
 * @date 2024-07-12 16:55:21
 */
public enum Gender {

    MALE(0, "男"),

    FEMALE(1, "女");

    private final Integer code;

    private final String desc;

    Gender(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Gender getInstance(Integer code) {
        for (Gender gender : Gender.values()) {
            if (gender.getCode().equals(code)) return gender;
        }
        return null;
    }
}
