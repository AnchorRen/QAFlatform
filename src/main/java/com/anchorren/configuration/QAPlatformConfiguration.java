package com.anchorren.configuration;

import com.anchorren.interceptor.LoginRequiredInterceptor;
import com.anchorren.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册登录拦截器
 *
 * @author AnchorRen
 * @date 2016/7/30
 */
@Component
public class QAPlatformConfiguration  extends WebMvcConfigurerAdapter{

	@Autowired
	PassportInterceptor passportInterceptor;

	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册拦截器
		registry.addInterceptor(passportInterceptor);
		//当访问 "/user/*" 路径的时候走拦截器
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
}
