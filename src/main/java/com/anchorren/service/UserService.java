package com.anchorren.service;

import com.anchorren.dao.LoginTicketDao;
import com.anchorren.dao.UserDao;
import com.anchorren.model.LoginTicket;
import com.anchorren.model.User;
import com.anchorren.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/7/17.
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private LoginTicketDao loginTicketDao;

	public User getUser(int id) {
		return userDao.selectUserById(id);
	}

	public User selectUserByName(String name) {
		return userDao.selectUserByName(name);
	}

	public Map<String, String> register(String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(username)) {
			map.put("msg","用户名不能为空！");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg","密码不能为空！");
			return map;
		}
		User user = userDao.selectUserByName(username);
		if (user != null) {
			map.put("msg", "用户名已经被注册了！");
			return map;
		}
		user = new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0, 5));
		Random random = new Random();
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
		user.setPassword(MD5Util.MD5(password + user.getSalt()));//用盐加密后的密码
		userDao.addUser(user);

		//登陆
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket);

		return map;
	}


	public Map<String, String> login(String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(username)) {
			map.put("msg","用户名不能为空！");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg","密码不能为空！");
			return map;
		}
		User user = userDao.selectUserByName(username);
		if (user == null) {
			map.put("msg", "用户名不存在！");
			return map;
		}
		if (!MD5Util.MD5(password + user.getSalt()).equals(user.getPassword())) {
			map.put("msg", "密码不正确！");
			return map;
		}

		//登陆
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket);

		return map;
	}

	private String addLoginTicket(int userId) {

		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(userId);
		loginTicket.setStatus(0);
		Date date = new Date();
		date.setTime(date.getTime() + (3600 * 24 * 1000)); //毫秒
		loginTicket.setExpired(date);
		loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));

		loginTicketDao.addTicket(loginTicket);
		return loginTicket.getTicket();
	}

	public void logout(String ticket) {
		loginTicketDao.updateStatus(ticket,1);
	}

}
