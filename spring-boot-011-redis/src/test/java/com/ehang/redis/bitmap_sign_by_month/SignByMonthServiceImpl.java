package com.ehang.redis.bitmap_sign_by_month;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一行Java
 * @title: 按月签到
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 18:28
 */
@Slf4j
@Service
public class SignByMonthServiceImpl {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private int dayOfMonth() {
        DateTime dateTime = new DateTime();
        return dateTime.dayOfMonth().get();
    }

    /**
     * 按照月份和用户ID生成用户签到标识 UserId:Sign:560:2021-08
     *
     * @param userId 用户id
     * @return
     */
    private String signKeyWitMouth(String userId) {
        DateTime dateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");

        StringBuilder builder = new StringBuilder("UserId:Sign:");
        builder.append(userId).append(":")
                .append(dateTime.toString(fmt));
        return builder.toString();
    }

    /**
     * 设置标记位
     * 标记是否签到
     *
     * @param key
     * @param offset
     * @param tag
     * @return
     */
    public Boolean sign(String key, long offset, boolean tag) {
        return this.stringRedisTemplate.opsForValue().setBit(key, offset, tag);
    }


    /**
     * 统计计数
     *
     * @param key 用户标识
     * @return
     */
    public long bitCount(String key) {
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount(key.getBytes()));
    }

    /**
     * 获取多字节位域
     */
    public List<Long> bitfield(String buildSignKey, int limit, long offset) {
        return this.stringRedisTemplate
                .opsForValue()
                .bitField(buildSignKey, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset));
    }


    /**
     * 判断是否被标记
     *
     * @param key
     * @param offest
     * @return
     */
    public Boolean container(String key, long offest) {
        return this.stringRedisTemplate.opsForValue().getBit(key, offest);
    }


    /**
     * 用户今天是否签到
     *
     * @param userId
     * @return
     */
    public int checkSign(String userId) {
        DateTime dateTime = new DateTime();

        String signKey = this.signKeyWitMouth(userId);
        int offset = dateTime.getDayOfMonth() - 1;
        int value = this.container(signKey, offset) ? 1 : 0;
        return value;
    }


    /**
     * 查询用户当月签到日历
     *
     * @param userId
     * @return
     */
    public Map<String, Boolean> querySignedInMonth(String userId) {
        DateTime dateTime = new DateTime();
        int lengthOfMonth = dateTime.dayOfMonth().getMaximumValue();
        Map<String, Boolean> signedInMap = new HashMap<>(dateTime.getDayOfMonth());

        String signKey = this.signKeyWitMouth(userId);
        List<Long> bitfield = this.bitfield(signKey, lengthOfMonth, 0);
        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);

            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            for (int i = lengthOfMonth; i > 0; i--) {

                DateTime dateTime1 = dateTime.withDayOfMonth(i);
                signedInMap.put(dateTime1.toString(fmt), signFlag >> 1 << 1 != signFlag);
                signFlag >>= 1;
            }
        }
        return signedInMap;
    }


    /**
     * 用户签到
     *
     * @param userId
     * @return
     */
    public boolean signWithUserId(String userId) {
        int dayOfMonth = this.dayOfMonth();
        String signKey = this.signKeyWitMouth(userId);
        long offset = (long) dayOfMonth - 1;
        boolean re = false;
        if (Boolean.TRUE.equals(this.sign(signKey, offset, Boolean.TRUE))) {
            re = true;
        }

        // 查询用户连续签到次数,最大连续次数为7天
        long continuousSignCount = this.queryContinuousSignCount(userId, 7);
        return re;
    }

    /**
     * 统计当前月份一共签到天数
     *
     * @param userId
     */
    public long countSignedInDayOfMonth(String userId) {
        String signKey = this.signKeyWitMouth(userId);
        return this.bitCount(signKey);
    }


    /**
     * 查询用户当月连续签到次数
     *
     * @param userId
     * @return
     */
    public long queryContinuousSignCountOfMonth(String userId) {
        int signCount = 0;
        String signKey = this.signKeyWitMouth(userId);
        int dayOfMonth = this.dayOfMonth();
        List<Long> bitfield = this.bitfield(signKey, dayOfMonth, 0);

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


    /**
     * 以7天一个周期连续签到次数
     *
     * @param period 周期
     * @return
     */
    public long queryContinuousSignCount(String userId, Integer period) {
        //查询目前连续签到次数
        long count = this.queryContinuousSignCountOfMonth(userId);
        //按最大连续签到取余
        if (period != null && period < count) {
            long num = count % period;
            if (num == 0) {
                count = period;
            } else {
                count = num;
            }
        }
        return count;
    }
}