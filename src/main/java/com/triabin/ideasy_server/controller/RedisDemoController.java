package com.triabin.ideasy_server.controller;

import com.triabin.ideasy_server.common.dto.Response;
import com.triabin.ideasy_server.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：Redis使用演示接口
 *
 * @author Triabin
 * @date 2025-06-09 17:20:19
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/redis")
public class RedisDemoController {

    private static final Logger logger = LogManager.getLogger(RedisDemoController.class);

    private final IRedisService redisService;

    @GetMapping("/set")
    public Response<String> set(@RequestParam String key, @RequestParam Integer value) {
        try {
            redisService.set(key, value);
            logger.info("Redis设值【key:{} value:{}】成功！！", key, value);
            return Response.success();
        } catch (Exception e) {
            logger.error("Redis入值异常", e);
            return Response.error();
        }
    }

    @GetMapping("/get")
    public Response<Object> get(@RequestParam String key) {
        try {
            Object value = redisService.get(key);
            logger.info("Redis取值【key:{} value:{}】成功！！", key, value);
            return Response.success(value);
        } catch (Exception e) {
            logger.error("Redis入值异常", e);
            return Response.error();
        }
    }
}
