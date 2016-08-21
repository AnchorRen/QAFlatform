package com.anchorren.service;

import com.anchorren.utils.JedisUtil;
import com.anchorren.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 关注服务
 *
 * @author AnchorRen
 * @date 2016/8/20
 */
@Service
public class FollowService {

	@Autowired
	private JedisUtil jedisUtil;


	/**
	 * 用户关注某一类实体
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean follow(int userId, int entityType, int entityId) {
		//获取关注列表key和粉丝key
		String foollowerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String foolloweeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

		Date date = new Date();

		Jedis jedis = jedisUtil.getJedis();
		//开启事务
		Transaction tx = jedisUtil.multi(jedis);
		//粉丝列表和关注列表更新
		tx.zadd(foollowerKey, date.getTime(), String.valueOf(userId));
		tx.zadd(foolloweeKey, date.getTime(), String.valueOf(entityId));

		List<Object> result = jedisUtil.exec(tx, jedis);

		return result.size() == 2 && (Long) result.get(0) > 0 && (Long) result.get(1) > 0;

	}

	/**
	 * 取消关注某一类实体
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean unFollow(int userId, int entityType, int entityId) {
		//获取关注列表key和粉丝key
		String foollowerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String foolloweeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

		Date date = new Date();

		Jedis jedis = jedisUtil.getJedis();
		//开启事务
		Transaction tx = jedisUtil.multi(jedis);
		//粉丝列表和关注列表更新
		tx.zrem(foollowerKey, String.valueOf(userId));
		tx.zrem(foolloweeKey, String.valueOf(entityId));

		List<Object> result = jedisUtil.exec(tx, jedis);

		return result.size() == 2 && (Long) result.get(0) > 0 && (Long) result.get(1) > 0;

	}

	private List<Integer> getIdsFromSet(Set<String> idsets) {
		List<Integer> ids = new ArrayList<>();
		for (String idset : idsets) {
			ids.add(Integer.parseInt(idset));
		}
		return ids;
	}

	/**
	 * 获取粉丝
	 * @param entityType 实体类型
	 * @param entityId 实体Id
	 * @param count 获取的数量
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int count) {

		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisUtil.zrevrange(followerKey, 0, count));
	}


	/**
	 * 获取粉丝
	 * @param entityType 实体类型
	 * @param entityId 实体Id
	 * @param offset 分页起始
	 * @param count 获取的数量
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {

		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisUtil.zrevrange(followerKey, offset, count));
	}

	/**
	 * 获取关注者
	 * @param entityType 实体类型
	 * @param entityId 实体Id
	 * @param count 获取的数量
	 * @return
	 */
	public List<Integer> getFollowees(int entityType, int entityId, int count) {

		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return getIdsFromSet(jedisUtil.zrevrange(followeeKey, 0, count));
	}


	/**
	 * 获取关注者
	 * @param entityType 实体类型
	 * @param entityId 实体Id
	 * @param offset 分页起始
	 * @param count 获取的数量
	 * @return
	 */
	public List<Integer> getFollowees(int entityType, int entityId, int offset, int count) {

		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return getIdsFromSet(jedisUtil.zrevrange(followeeKey, offset, count));
	}

	public long getFollowerCount(int entityType, int entityId) {

		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisUtil.zcard(followerKey);

	}

	public long getFolloweeCount(int entityType, int entityId) {

		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return jedisUtil.zcard(followeeKey);

	}

	public boolean isFollower(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisUtil.zscore(followerKey, String.valueOf(userId)) != null ;
	}

}


