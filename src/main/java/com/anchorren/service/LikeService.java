package com.anchorren.service;

import com.anchorren.utils.JedisUtil;
import com.anchorren.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author AnchorRen
 * @date 2016/8/3
 */
@Service
public class LikeService {

	@Autowired
	JedisUtil jedisUtil;

	/**
	 * 赞功能
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long like(int userId, int entityType, int entityId) {

		//在赞key中添加
		String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
		jedisUtil.sadd(likeKey, String.valueOf(userId));
		//在踩key中移除
		String dislikeKey = RedisKeyUtil.getBizDisLikeKey(entityType,entityId);
		jedisUtil.srem(dislikeKey, String.valueOf(userId));

		return jedisUtil.acard(likeKey); //返回获得的赞的数量

	}

	/**
	 * 踩功能
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long dislike(int userId, int entityType, int entityId) {
		//踩key中添加
		String dislikeKey = RedisKeyUtil.getBizDisLikeKey(entityType,entityId);
		jedisUtil.sadd(dislikeKey, String.valueOf(userId));
		//赞key中移除
		String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
		jedisUtil.srem(likeKey, String.valueOf(userId));

		return jedisUtil.acard(likeKey); //返回获得的赞的数量

	}

	/**
	 * 获得用户的踩赞状态
	 * @param userId 要判断的用户id
	 * @param entityType 类型
	 * @param entityId Id
	 * @return 1：赞；-1：踩；0：未踩未赞
	 */
	public int getLikeStatus(int userId, int entityType, int entityId) {

		String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
		if(jedisUtil.sismember(likeKey,String.valueOf(userId))){
			return 1;
		}
		String dislikeKey = RedisKeyUtil.getBizDisLikeKey(entityType,entityId);
		return jedisUtil.sismember(dislikeKey,String.valueOf(userId))? -1 : 0;
	}

	/**
	 * 获取赞的数量
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long getLikeCont(int entityType, int entityId ){

		String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
		return jedisUtil.acard(likeKey); //返回获得的赞的数量
	}

}
