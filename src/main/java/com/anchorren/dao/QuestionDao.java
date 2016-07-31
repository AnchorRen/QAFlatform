package com.anchorren.dao;

import com.anchorren.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2016/7/17.
 */
@Mapper
public interface QuestionDao {

	String TABLE_NAME = " question";
	String INSERT_FIELDS = " title,content,created_date,user_id,comment_count ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (" +
			"#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
	int addQuestion(Question question);

	List<Question> selectLatestQuestions(@Param("userId") int userId,
										@Param("offset") int offset,
										@Param("limit") int limit);

	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
	Question getQuestionById(int id);


	@Update({"update ",TABLE_NAME, " set comment_count = #{commentCount} where id = #{id}"})
	int updateCommmentCount(@Param("id") int id,@Param("commentCount")int commentCount);

}
