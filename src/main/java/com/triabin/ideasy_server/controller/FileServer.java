package com.triabin.ideasy_server.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 类描述：文件服务接口
 *
 * @author Triabin
 * @date 2025-08-11 11:31:51
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileServer {

    private final static Logger logger = LogManager.getLogger(FileServer.class);

    @GetMapping("/ebook")
    public void ebook(HttpServletResponse response) {
        File file = new File("/Users/dawnlee/Pictures/电脑壁纸/snow_bg.jpg");
        try (InputStream is = new FileInputStream(file)) {
            String fileName = file.getName();
            String fileType = new Tika().detect(file);
            if (StringUtils.isBlank(fileType)) {
                fileType = "application/octet-stream";
            }
            byte[] bytes = new byte[4096];
            int readLen;
            response.reset();
            response.setContentType(fileType + ";charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            response.addHeader("Cache-Control", "no-cache");
            // 配置允许跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Headers", "*");
            while ((readLen = is.read(bytes)) != -1) {
                response.getOutputStream().write(bytes, 0, readLen);
            }
        } catch (Exception e) {
            logger.error("文件服务异常", e);
        }
    }
}
