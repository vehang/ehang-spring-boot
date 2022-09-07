package com.ehang.redis.lock.byannotation.enums;

/**
 * @author LENOVO
 * @title: RedisLockTypeEnum
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/9/7 10:08
 */
public enum RedisLockTypeEnum {
    /**
     * 自定义 key 前缀
     */
    ONE("Business1", "Test1"),

    TWO("Business2", "Test2"),
    ORDER_MANA("Order Management", "Order Management"),
    ;
    private String code;
    private String desc;
    RedisLockTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    public String getUniqueKey(String key) {
        return String.format("%s:%s", this.getCode(), key);
    }
}
