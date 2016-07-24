package com.anchorren.service;

import com.anchorren.dao.UserDao;
import com.anchorren.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/7/17.
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	public User getUser(int id){
		return userDao.selectUserById(id);
	}
}
