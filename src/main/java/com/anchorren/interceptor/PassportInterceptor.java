package com.anchorren.interceptor;

import com.anchorren.dao.LoginTicketDao;
import com.anchorren.dao.UserDao;
import com.anchorren.model.HostHolder;
import com.anchorren.model.LoginTicket;
import com.anchorren.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author AnchorRen
 * @date 2016/7/30
 */
@Component
public class PassportInterceptor  implements HandlerInterceptor{

	@Autowired
	LoginTicketDao loginTicketDao;

	@Autowired
	UserDao userDao;

	@Autowired
	HostHolder hostHolder;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		hostHolder.clear();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ticket = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}

		if (ticket != null) {
			LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
			if (loginTicket == null || loginTicket.getExpired().before(new Date()) ||
					loginTicket.getStatus() != 0) {
				return true;
			}

			//用户信息有效，取出用户
			User user = userDao.selectUserById(loginTicket.getUserId());
			hostHolder.setUser(user); //把取出的User用户放到ThreadLocal中

		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		if (modelAndView != null) {
			//在所有页面渲染之前，加入User。则所有经过拦截器的页面都能访问到user信息了
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}
}
