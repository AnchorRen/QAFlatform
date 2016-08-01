package com.anchorren.dao;

import com.anchorren.model.Comment;
import org.apache.ibatis.annotations.*;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/23
 */
@Mapper
public interface CommentDao {

	String TABLE_NAME = " comment ";
	String INSERT_FIELDS = " user_id, content,entity_id,entity_type,created_date,status ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS; //小心逗号

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values ( #{userId},#{content},#{entityId},#{entityType}," +
			"#{createdDate},#{status})"})
	int addComment(Comment comment);

	@Update({"update ",TABLE_NAME," set status = #{status} where entity_id = #{entityId} and " +
			"entity_type = #{entityType}"})
	void updateStatus(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status") int status);

	/*@Update({"update ",TABLE_NAME," set status = #{status} where id = #{id}"})
	void updateStatus(@Param("id") int id,@Param("status") int status);*/

	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
	List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

	@Select({"select count(*) from ",TABLE_NAME," where entity_id = #{entityId} and entity_type = #{entityType}"})
	int getCommentCount(@Param("entityId") int entityId,@Param("entityType")int entityType);


}
