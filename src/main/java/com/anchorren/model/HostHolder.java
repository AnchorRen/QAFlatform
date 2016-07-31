package com.anchorren.model;

import org.springframework.stereotype.Component;

/**
 * 使用ThreadLocal存放取出的用户信息
 *
 * @author AnchorRen
 * @date 2016/7/30
 */
@Component
public class HostHolder {

	private static ThreadLocal<User> users  = new ThreadLocal<>();

	public User getUser() {
		return users.get();
	}

	public void setUser(User user) {
		users.set(user);

	}

	public void clear() {
		users.remove();
	}

}
