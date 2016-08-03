package com.anchorren.utils;

/**
 * 	Redis key 统一生成工具
 *
 * @author AnchorRen
 * @date 2016/8/3
 */
public class RedisKeyUtil {

	private static String SPLIT = ":";
	private static String BIZ_LIKE = "LIKE";
	private static String BIZ_DISLIKE = "DISLIKE";

	/**
	 * 生成赞的key
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getLikeKey(int entityType, int entityId) {
		return BIZ_LIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
	}

	/**
	 * 生成踩的key
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getBizDisLikeKey(int entityType, int entityId) {
		return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
	}

}
