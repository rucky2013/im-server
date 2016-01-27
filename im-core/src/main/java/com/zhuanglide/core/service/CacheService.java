package com.zhuanglide.core.service;

import java.util.List;

/**
 * Created by wwj on 16.1.26.
 */
public interface CacheService {
    /**
     * 返回Redis中的Key值对应的字符串
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 根据返回的Key值得字符串值，转换成type类型的对象返回
     *
     * @param key
     * @param type
     * @return
     */
    <T> T get(String key, Class<T> type);

    /**
     * 往Redis里面增加键值对
     *
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * 存放的键值对为对象类型
     *
     * @param key
     * @param value
     * @return
     */
    <T> String set(String key, T value);

    /**
     * 删除键值对
     *
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * 检查键值对是否存在
     *
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 配置属性失效
     *
     * @param key
     *            键值
     * @param seconds
     *            过期，单位秒数
     * @return 返回过期时间
     */
    Long expire(final String key, final int seconds);

    // 对Redis的Hash进行操作
    /**
     * 获取Redis中的为key的hash的Map，然后返回map的field字段对应的value值字符串
     *
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * 获取Redis中的为key的hash的Map，然后返回map的field字段对应的value值字符串转换成type类型
     *
     * @param key
     * @param field
     * @param type
     * @return
     */
    <T> T hget(String key, String field, Class<?> type);

    /**
     * 往hash中新增加键值对
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hset(String key, String field, String value);

    /**
     * 往hash中新增键值对，为对象类型
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    <T> Long hset(String key, String field, T value);

    /**
     * 删除hash键值对中的field
     *
     * @param key
     * @param field
     * @return
     */
    Long hdel(String key, String field);

    /**
     * 检查hash中是否包含对应的field
     *
     * @param key
     * @param field
     * @return
     */
    Boolean hexists(String key, String field);

    /**
     * 获取hash中的多个fields的值
     *
     * @param key
     * @param fields
     * @return
     */
    List<String> hmget(String key, String... fields);

    /**
     * 获取hash中的多个fields的值,返回对象列表
     *
     * @param key
     * @param type
     * @param fields
     * @return
     */
    <T> List<T> hmget(String key, Class<?> type, String... fields);

}
