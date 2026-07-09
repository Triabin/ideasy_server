package com.triabin.ideasy_server.service;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

import java.nio.charset.StandardCharsets;

/**
 * 类描述：仿谷歌布隆过滤器实现
 *
 * @author Triabin
 * @date 2025-06-12 18:03:08
 */
@RequiredArgsConstructor
public class RedisBloomFilter {

    private static final Logger logger = LogManager.getLogger(RedisBloomFilter.class);

    private final IRedisService redisService;

    public final static String RS_BF_NS = "rbf:";

    /**
     * 预估元素数量
     */
    private int numApproxElements;

    /**
     * 可接受的最大误差
     */
    private double fpp;

    /**
     * 自动计算的哈希函数个数
     */
    private int numHashFunctions;

    /**
     * 自动计算的最优BitMap长度
     */
    private int bitmapLength;

    /**
     * 方法描述：构造布隆过滤器
     *
     * @param numApproxElements 预估元素数量
     * @param fpp               可接受最大误差
     * @return {@link RedisBloomFilter}
     * @date 2025-06-12 18:07:02
     */
    public RedisBloomFilter init(int numApproxElements, double fpp) {
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        // 计算位数组长度
        this.bitmapLength = (int) (-numApproxElements * Math.log(fpp) / (Math.log(2) * Math.log(2)));
        // 计算哈希函数个数
        this.numHashFunctions = Math.max(1, (int) Math.round((double) bitmapLength / numApproxElements * Math.log(2)));
        return this;
    }

    /**
     * 方法描述：计算一个元素值哈希映射到BitMap的那些bit上，用两个hash函数来模拟多个hash函数的情况
     *
     * @param element 元素值
     * @return {@link long[]} bit数组下标
     * @date 2025-06-12 22:28:02
     */
    private long[] getBitIndices(String element) {
        long[] indices = new long[numHashFunctions];
        // 将传入的字符串转为一个128位的hash值，并且转化为一个byte数组
        byte[] bytes = Hashing.murmur3_128()
                .hashString(element, StandardCharsets.UTF_8)
                .asBytes();
        long hash1 = Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        long hash2 = Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);

        // 用这两个hash值来模拟多个函数产生的值
        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            indices[i] = (combinedHash & Long.MAX_VALUE) % bitmapLength;
            combinedHash = combinedHash + hash2;
        }
        return indices;
    }

    /**
     * 方法描述：插入元素
     *
     * @param key       原始Redis键，回自动拼接前缀
     * @param element   原始值，字符串类型
     * @param expireSec 过期时间（秒）
     * @date 2025-06-12 22:29:04
     */
    public void insert(String key, String element, int expireSec) {
        if (key == null || element == null) {
            throw new RuntimeException("键值均不能为空");
        }
        String actualKey = RS_BF_NS.concat(key);
        try {
            SessionCallback<Boolean> callback = new SessionCallback<>() {
                @Override
                public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                    @SuppressWarnings("unchecked")
                    ValueOperations<String, String> valueOperations = (ValueOperations<String, String>) operations.opsForValue();
                    for (long index : getBitIndices(element)) {
                        valueOperations.setBit(actualKey, index, true);
                    }
                    return null;
                }
            };
            redisService.pipeline(callback);
            redisService.expire(actualKey, expireSec);
        } catch (Exception e) {
            logger.error("Redis插入元素值异常", e);
        }
    }

    /**
     * 方法描述：检查元素在集合中是否（可能）存在
     *
     * @param key     原始Redis键，会拼接前缀
     * @param element 元素值，字符串类型
     * @return {@link boolean}
     * @date 2025-06-12 22:42:57
     */
    public boolean mayExist(String key, String element) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(element)) {
            throw new RuntimeException("键值均不能为空");
        }
        String actualKey = RS_BF_NS.concat(key);
        boolean result = false;
        try {
            SessionCallback<Boolean> callback = new SessionCallback<>() {
                @Override
                public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                    @SuppressWarnings("unchecked")
                    ValueOperations<String, String> valueOperations = (ValueOperations<String, String>) operations.opsForValue();
                    for (long index : getBitIndices(element)) {
                        valueOperations.getBit(actualKey, index);
                    }
                    return null;
                }
            };
            result = redisService.pipeline(callback).contains(Boolean.FALSE);
        } catch (Exception e) {
            logger.error("判断【{}】键的【{}】元素是否存在运行异常", key, element, e);
        }
        return result;
    }

    public RedisBloomFilter(IRedisService redisService, int numApproxElements, double fpp, int numHashFunctions, int bitmapLength) {
        this.redisService = redisService;
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        this.numHashFunctions = numHashFunctions;
        this.bitmapLength = bitmapLength;
    }

    @Override
    public String toString() {
        return "RedisBloomFilter{" +
                "redisService=" + redisService +
                ", numApproxElements=" + numApproxElements +
                ", fpp=" + fpp +
                ", numHashFunctions=" + numHashFunctions +
                ", bitmapLength=" + bitmapLength +
                '}';
    }
}
