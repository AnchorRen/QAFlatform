package com.anchorren.dao;

import com.anchorren.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2016/7/17.
 */
@Mapper
public interface UserDao {

	//注意空格
	String TABLE_NAME = " user ";
	String INSERT_FIELDS = " name, password, salt, head_url ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	/**
	 * 插入用户
	 * @param user
	 * @return
	 */
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (#{name}," +
			"#{password},#{salt},#{headUrl})"})
	int addUser(User user);

	/**
	 * 查询用户
	 * @param id
	 * @return
	 */
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id=#{id}"})
	User selectUserById(int id);

	/**
	 * 修改密码
	 * @param user
	 */
	@Update({"update ",TABLE_NAME,"set password=#{password} where id=#{id}"})
	void updatePassword(User user);

	/**
	 * 删除用户
	 * @param id
	 */
	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	void deleteUserById(int id);

}
