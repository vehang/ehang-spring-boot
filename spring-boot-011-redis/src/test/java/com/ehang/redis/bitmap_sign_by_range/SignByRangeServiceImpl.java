package com.ehang.redis.bitmap_sign_by_range;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一行Java
 * @title: SignByRangeServiceImpl
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 12:27
 */
@Slf4j
@Service
public class SignByRangeServiceImpl {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 根据区间的id 以及用户id 拼接key
     *
     * @param rangeId 区间ID 一般是指定活动的ID等
     * @param userId  用户的ID
     * @return
     */
    private String signKey(Integer rangeId, Integer userId) {
        StringBuilder builder = new StringBuilder("RangeId:Sign:");
        builder.append(rangeId).append(":")
                .append(userId);
        return builder.toString();
    }

    /**
     * 获取当前时间与起始时间的间隔天数
     *
     * @param start 起始时间
     * @return
     */
    private int intervalTime(LocalDateTime start) {
        return (int) (LocalDateTime.now().toLocalDate().toEpochDay() - start.toLocalDate().toEpochDay());
    }

    /**
     * 设置标记位
     * 标记是否签到
     *
     * @param key    签到的key
     * @param offset 偏移量 一般是指当前时间离起始时间（活动开始）的天数
     * @param tag    是否签到  true:签到  false:未签到
     * @return
     */
    private Boolean setBit(String key, long offset, boolean tag) {
        return this.stringRedisTemplate.opsForValue().setBit(key, offset, tag);
    }

    /**
     * 统计计数
     *
     * @param key 统计的key
     * @return
     */
    private long bitCount(String key) {
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount(key.getBytes()));
    }

    /**
     * 获取多字节位域
     *
     * @param key    缓存的key
     * @param limit  获取多少
     * @param offset 偏移量是多少
     * @return
     */
    private List<Long> bitfield(String key, int limit, long offset) {
        return this.stringRedisTemplate
                .opsForValue()
                .bitField(key, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset));
    }

    /**
     * 判断是否签到
     *
     * @param key    缓存的key
     * @param offest 偏移量 指当前时间距离起始时间的天数
     * @return
     */
    private Boolean container(String key, long offest) {
        return this.stringRedisTemplate.opsForValue().getBit(key, offest);
    }

    /**
     * 根据起始时间进行签到
     *
     * @param rangeId
     * @param userId
     * @param start
     * @return
     */
    public Boolean sign(Integer rangeId, Integer userId, LocalDateTime start) {
        int offset = intervalTime(start);
        String key = signKey(rangeId, userId);
        return setBit(key, offset, true);
    }

    /**
     * 根据偏移量签到
     *
     * @param rangeId
     * @param userId
     * @param offset
     * @return
     */
    public Boolean sign(Integer rangeId, Integer userId, long offset) {
        String key = signKey(rangeId, userId);
        return setBit(key, offset, true);
    }

    /**
     * 用户今天是否签到
     *
     * @param userId
     * @return
     */
    public Boolean checkSign(Integer rangeId, Integer userId, LocalDateTime start) {
        long offset = intervalTime(start);
        String key = this.signKey(rangeId, userId);
        return this.container(key, offset);
    }

    /**
     * 统计当前月份一共签到天数
     *
     * @param userId
     */
    public long countSigned(Integer rangeId, Integer userId) {
        String signKey = this.signKey(rangeId, userId);
        return this.bitCount(signKey);
    }

    public Map<String, Boolean> querySigned(Integer rangeId, Integer userId, LocalDateTime start) {
        int days = intervalTime(start);
        Map<String, Boolean> signedInMap = new HashMap<>(days);

        String signKey = this.signKey(rangeId, userId);
        List<Long> bitfield = this.bitfield(signKey, days + 1, 0);
        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = days; i >= 0; i--) {
                LocalDateTime localDateTime = start.plusDays(i);
                signedInMap.put(localDateTime.format(fmt), signFlag >> 1 << 1 != signFlag);
                signFlag >>= 1;
            }
        }
        return signedInMap;
    }

    /**
     * 查询用户当月连续签到次数
     *
     * @param userId
     * @return
     */
    public long queryContinuousSignCount(Integer rangeId, Integer userId, LocalDateTime start) {
        int signCount = 0;
        String signKey = this.signKey(rangeId, userId);
        int days = this.intervalTime(start);
        List<Long> bitfield = this.bitfield(signKey, days + 1, 0);

        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);
            DateTime dateTime = new DateTime();
            // 连续不为0即为连续签到次数，当天未签到情况下
            for (int i = 0; i < dateTime.getDayOfMonth(); i++) {
                if (signFlag >> 1 << 1 == signFlag) {
                    if (i > 0) break;
                } else {
                    signCount += 1;
                }
                signFlag >>= 1;
            }
        }
        return signCount;
    }
}
