package com.anchorren.controller;

import com.anchorren.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author AnchorRen
 * @date 2016/7/27
 */
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserService userService;


	@RequestMapping(path = {"/reg/",}, method = {RequestMethod.POST})

	public String reg(Model model,
					  @RequestParam("username") String username,
					  @RequestParam("password") String password,
					  @RequestParam(value = "rememberme",defaultValue = "fasle") boolean rememberme,
					  HttpServletResponse response) {


		try {
			Map<String, String> map = userService.register(username, password);
			if (map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
			}
			if (map.containsKey("msg")) {
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}

			return "redirect:/";
		} catch (Exception e) {
			logger.error("注册异常！" + e.getMessage());
				model.addAttribute("msg", "服务器错误！");
			return "login";
		}
	}

	@RequestMapping(path = {"/relogin"}, method = {RequestMethod.GET})
	public String reg(Model model) {
		return "login";
	}

	@RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
	public String login(Model model,
						@RequestParam("username") String username,
						@RequestParam("password") String password,
						@RequestParam(value="rememberme",defaultValue = "false") boolean rememberme,
								HttpServletResponse response){
		Map<String, String> map = userService.login(username, password);
		try {
			if (map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
			}
			if (map.containsKey("msg")) {
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}

			return "redirect:/";
		} catch (Exception e) {
			logger.error("注册异常！" + e.getMessage());
			return "login";
		}
	}


}