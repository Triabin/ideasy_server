package com.triabin.ideasy_server.common;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

/**
 * 类描述：自定义文件工具类
 *
 * @author Triabin
 * @date 2025-12-11 14:40:53
 */
public class MyFileUtils {
    /**
     * 方法描述：获取文件类型
     *
     * @param path 要获取MimeType的文件路径
     * @return {@link String} MimeType
     * @throws IOException 文件读取异常
     * @date 2025-12-11 14:43:10
     */
    public static String getMimeType(String path) throws IOException {
        File file = new File(path);
        Tika tika = new Tika();
        return tika.detect(file);
    }

    /**
     * 方法描述：获取文件类型
     *
     * @param file 要获取MimeType的文件对象
     * @return {@link String} MimeType
     * @throws IOException 文件读取异常
     * @date 2025-12-11 14:46:15
     */
    public static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
