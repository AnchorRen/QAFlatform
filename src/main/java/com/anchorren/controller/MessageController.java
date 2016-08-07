package com.anchorren.controller;

import com.anchorren.model.HostHolder;
import com.anchorren.model.Message;
import com.anchorren.model.User;
import com.anchorren.model.ViewObject;
import com.anchorren.service.MessageService;
import com.anchorren.service.UserService;
import com.anchorren.utils.QAUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.resources.Messages_es;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/31
 */
@Controller
public class MessageController {

	Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	MessageService messageService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	UserService userService;

	/**
	 * 显示私信详情
	 * @param model
	 * @param conversationId
	 * @return
	 */
	@RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
	public String conversionDetail(Model model,
								   @Param("conversationId") String conversationId) {

		try {
			List<Message> conversionDetail = messageService.getConversionDetail(conversationId, 0, 10);
			//设置为已读
			messageService.updateHasRead(hostHolder.getUser().getId(), conversationId);
			List<ViewObject> messages = new ArrayList<>();
			for (Message message : conversionDetail) {
				ViewObject vo = new ViewObject();
				vo.set("message",message);
				User user =  userService.getUser(message.getFromId());

				if (user == null) {
					continue;
				}

				vo.set("headUrl", user.getHeadUrl());
				vo.set("user", user);
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
		} catch (Exception e) {
			logger.error("发送私信失败！" + e.getMessage());
		}
		return "letterDetail";


	}

	@RequestMapping(value = "/msg/addMessage", method = RequestMethod.POST)
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName,
							 @RequestParam("content") String content) {
		try {
			if (hostHolder.getUser() == null) {
				return QAUtil.getJSONString(999, "未登录");
			}
			User user = userService.selectUserByName(toName);
			if (user == null) {
				return QAUtil.getJSONString(1,"用户不存在！");
			}

			Message message = new Message();
			message.setContent(content);
			message.setFromId(hostHolder.getUser().getId());
			message.setCreatedDate(new Date());
			message.setToId(user.getId());

			messageService.addMessage(message);
			return QAUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("发送私信失败！"+e.getMessage());
			return QAUtil.getJSONString(1, "发送私信失败！");
		}
	}


	@RequestMapping(value = "/msg/jsonAddMessage", method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
							 @RequestParam("toId") int toId,
							 @RequestParam("content") String content) {
		try {
			Message message = new Message();
			message.setContent(content);
			message.setFromId(fromId);
			message.setToId(toId);
			message.setCreatedDate(new Date());
			messageService.addMessage(message);

			return QAUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("发送私信失败！" + e.getMessage());
			return QAUtil.getJSONString(1, "发送私信失败！");
		}

	}

	@RequestMapping(value = "/msg/list", method = {RequestMethod.GET})
	public String getconversationList(Model model) {
		try {
			if (hostHolder.getUser() == null) {
				return "redirect:/reglogin";
			}
			int localUser = hostHolder.getUser().getId();
			List<ViewObject> conversations = new ArrayList<>();
			List<Message> messages = messageService.getConversationList(localUser, 0, 10);
			for (Message message : messages) {

				ViewObject vo = new ViewObject();
				vo.set("message", message);
				int targetId = message.getFromId() == localUser ? message.getToId() : message.getFromId();
				User user = userService.getUser(targetId);
				vo.set("user", user);
				vo.set("unread", messageService.getConversationUnReadCount(localUser, message.getConversationId()));
				conversations.add(vo);
			}
			model.addAttribute("conversations", conversations);
			return "letter";
		} catch (Exception e) {
			logger.error("获取站内信列表失败！" + e.getMessage());
		}

		return "letter";

	}



}
