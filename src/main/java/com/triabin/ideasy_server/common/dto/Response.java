package com.triabin.ideasy_server.common.dto;

/**
 * 类描述：前后端交互响应类
 * @author Triabin
 * @date 2024-07-14 13:46:20
 */
public class Response<T> {

    /**
     * 响应码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 响应携带数据
     */
    private T data;

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response<>(200, "success", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "success", data);
    }

    public static <T> Response<T> success(String msg, T data) {
        return new Response<>(200, msg, data);
    }

    public static <T> Response<T> fail() {
        return new Response<>(400, "fail", null);
    }

    public static <T> Response<T> fail(String msg) {
        return new Response<>(400, msg, null);
    }

    public static <T> Response<T> error() {
        return new Response<>(500, "error", null);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>(500, msg, null);
    }

    public int getCode() {
        return code;
    }

    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Response<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}
