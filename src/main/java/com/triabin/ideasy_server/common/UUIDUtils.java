package com.triabin.ideasy_server.common;

import java.util.UUID;

/**
 * 类描述：UUID工具类
 *
 * @author Triabin
 * @date 2025-05-31 15:24:41
 */
public class UUIDUtils {

    /**
     * 方法描述：生成UUID
     *
     * @return {@link String} UUID
     * @date 2025-05-31 15:25:35
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
