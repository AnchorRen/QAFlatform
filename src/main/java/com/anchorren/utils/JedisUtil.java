package com.anchorren.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author AnchorRen
 * @date 2016/8/3
 */
@Service
public class JedisUtil implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(JedisUtil.class);

	private JedisPool jedisPool;

	@Override
	public void afterPropertiesSet() throws Exception {
		jedisPool = new JedisPool("redis://localhost:6379/5");
	}

	/**
	 * 添加
	 * @param key
	 * @param value
	 */
	public void sadd(String key, String value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error("发生错误："+e.getMessage());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 求数量
	 * @param key
	 * @return
	 */
	public long acard(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long scard = jedis.scard(key);
			return scard;
		} catch (Exception e) {
			logger.error("发生错误："+e.getMessage());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}

	/**
	 * 移除
	 * @param key
	 * @param value
	 */
	public void srem(String key, String value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long srem = jedis.srem(key, value);
		} catch (Exception e) {
			logger.error("发生错误："+e.getMessage());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 是否是成员
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sismember(String key, String value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("发生错误："+e.getMessage());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}


























}
