package com.anchorren.dao;

import com.anchorren.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Mapper
public interface FeedDao {

	String TABLE_NAME = "feed";
	String INSERT_FIELDS = " user_id,data,created_date,type ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS; //小心逗号

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
			") values (#{userId},#{data},#{createdDate},#{type})"})
	int addFeed(Feed feed);

	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
	Feed getFeedById(int id);

	/**
	 * 查询新鲜事
	 * @param maxId 增量拉取
	 * @param userIds 如果登陆了，就是关注的用户id集合
	 * @param count 每次取多少记录
	 * @return
	 */
	List<Feed> selectUserFeeds(@Param("maxId") int maxId,
							   @Param("userIds") List<Integer> userIds,
							   @Param("count") int count);

}
