package com.anchorren.dao;

import com.anchorren.model.Message;
import org.apache.ibatis.annotations.*;
import org.apache.struts.tiles.TilesException;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/31
 */
@Mapper
public interface MessageDao {

	String TABLE_NAME = " message ";
	String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, " +
			"created_date";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	/**
	 * 插入一条新的message
	 * @param message
	 * @return
	 */
	@Insert({"insert into ",TABLE_NAME, "(",INSERT_FIELDS,") values " +
			"(#{fromId},#{toId}, #{content}, #{hasRead}, #{conversationId},#{createdDate})"})
	int addMessage(Message message);


	/**
	 * 获取私信详情
	 *
	 * @return
	 */
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
			" where conversation_id = #{conversationId} order by id desc limit #{offset},#{limit}"})
	List<Message> getConversionDetail(@Param("conversationId") String conversationId,
									  @Param("offset") int offset,
									  @Param("limit") int limit);

/*	SELECT *,COUNT(*) cont FROM (SELECT * FROM message WHERE from_id = 12 OR to_id = 12 GROUP BY created_date DESC) tt
	GROUP BY conversation_id ORDER BY created_date DESC*/

	/**
	 * 获取私信列表
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Select({"select ",INSERT_FIELDS," , count(id) as id from "
			,"(select * from ",TABLE_NAME, " where from_id = #{userId} or to_id = #{userId} group by " +
			"created_date desc) tt","group by conversation_id order by created_date desc limit #{offset},#{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,
									  @Param("offset") int offset,
										@Param("limit") int limit);

	/**
	 * 查询未读信息数量
	 * @param userId
	 * @param conversationId
	 * @return
	 */
	@Select({" select count(id) from ", TABLE_NAME," where has_read = 0 and to_id = #{userId} " +
			"and conversation_id = #{conversationId}"})
	int getConversationUnReadCount(@Param("userId") int userId,
								   @Param("conversationId") String conversationId);

	@Update({"update ", TABLE_NAME, " set has_read = 1 where conversation_id = #{conversationId} and to_id = #{userId}"})
	void updateHasRead(@Param("conversationId") String conversationId,
					   @Param("userId") int userId);
}
