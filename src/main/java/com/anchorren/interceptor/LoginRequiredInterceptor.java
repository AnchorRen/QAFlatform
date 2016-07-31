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
public class LoginRequiredInterceptor implements HandlerInterceptor{

	@Autowired
	HostHolder hostHolder;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//未登录，则跳转到登录页面，加上回调URL
		if (hostHolder.getUser() == null) {
			response.sendRedirect("/reglogin?next="+request.getRequestURI());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}
}
