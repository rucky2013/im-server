package com.zhuanglide.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhuanglide.core.service.CacheService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwj on 16.1.26.
 */
public class CacheServiceRedisImpl implements CacheService {

    private final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    @Override
    public String get(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> type) {
        String result = get(key);
        if (result == null) {
            return null;
        }
        return (T) JSON.parseObject(result,type);
    }

    @Override
    public String set(String key, String value) {

        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public <T> String set(String key, T value) {
        return set(key, JSON.toJSON(value).toString());
    }

    @Override
    public Long del(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.del(key);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public String hget(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T hget(String key, String field, Class<?> type) {
        return (T) JSON.parseObject(hget(key, field), type);
    }

    @Override
    public Long hset(String key, String field, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public <T> Long hset(String key, String field, T value) {
        return hset(key, field, JSON.toJSON(value));

    }

    @Override
    public Boolean exists(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean hexists(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return Boolean.FALSE;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return new ArrayList<String>(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> hmget(String key, Class<?> type, String... fields) {
        List<String> reslut = hmget(key, fields);

        List<T> list = new ArrayList<T>(reslut.size());
        for (String jsonStr : reslut) {
            list.add((T) JSON.parseObject(jsonStr, type));
        }

        return list;
    }

    @Override
    public Long hdel(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hdel(key, field);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public Long expire(String key, int seconds) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("Execute redis command failure", e);
        } finally {
            jedis.close();
        }
        return null;
    }
}
