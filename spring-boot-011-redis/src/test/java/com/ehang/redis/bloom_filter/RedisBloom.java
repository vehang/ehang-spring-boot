package com.ehang.redis.bloom_filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * redis布隆过滤器
 *
 * @author 一行Java
 * @title: RedisBloom
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 17:03
 */
@Component
public class RedisBloom {

    private static RedisScript<Boolean> bfreserveScript = new DefaultRedisScript<>("return redis.call('bf.reserve', KEYS[1], ARGV[1], ARGV[2])", Boolean.class);
    private static RedisScript<Boolean> bfaddScript = new DefaultRedisScript<>("return redis.call('bf.add', KEYS[1], ARGV[1])", Boolean.class);
    private static RedisScript<Boolean> bfexistsScript = new DefaultRedisScript<>("return redis.call('bf.exists', KEYS[1], ARGV[1])", Boolean.class);
    private static String bfmaddScript = "return redis.call('bf.madd', KEYS[1], %s)";
    private static String bfmexistsScript = "return redis.call('bf.mexists', KEYS[1], %s)";
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 设置错误率和大小（需要在添加元素前调用，若已存在元素，则会报错）
     * 错误率越低，需要的空间越大
     *
     * @param key
     * @param errorRate   错误率，默认0.01
     * @param initialSize 默认100，预计放入的元素数量，当实际数量超出这个数值时，误判率会上升，尽量估计一个准确数值再加上一定的冗余空间
     * @return
     */
    public Boolean bfreserve(String key, double errorRate, int initialSize) {
        return redisTemplate.execute(bfreserveScript, Arrays.asList(key), String.valueOf(errorRate), String.valueOf(initialSize));
    }

    /**
     * 添加元素
     *
     * @param key
     * @param value
     * @return true表示添加成功，false表示添加失败（存在时会返回false）
     */
    public Boolean bfadd(String key, String value) {
        return redisTemplate.execute(bfaddScript, Arrays.asList(key), value);
    }

    /**
     * 查看元素是否存在（判断为存在时有可能是误判，不存在是一定不存在）
     *
     * @param key
     * @param value
     * @return true表示存在，false表示不存在
     */
    public Boolean bfexists(String key, String value) {
        return redisTemplate.execute(bfexistsScript, Arrays.asList(key), value);
    }

    /**
     * 批量添加元素
     *
     * @param key
     * @param values
     * @return 按序 1表示添加成功，0表示添加失败
     */
    public List<Integer> bfmadd(String key, String... values) {
        return (List<Integer>) redisTemplate.execute(this.generateScript(bfmaddScript, values), Arrays.asList(key), values);
    }

    /**
     * 批量检查元素是否存在（判断为存在时有可能是误判，不存在是一定不存在）
     *
     * @param key
     * @param values
     * @return 按序 1表示存在，0表示不存在
     */
    public List<Integer> bfmexists(String key, String... values) {
        return (List<Integer>) redisTemplate.execute(this.generateScript(bfmexistsScript, values), Arrays.asList(key), values);
    }

    private RedisScript<List> generateScript(String script, String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= values.length; i++) {
            if (i != 1) {
                sb.append(",");
            }
            sb.append("ARGV[").append(i).append("]");
        }
        return new DefaultRedisScript<>(String.format(script, sb.toString()), List.class);
    }

}
