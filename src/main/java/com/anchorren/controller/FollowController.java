package com.anchorren.controller;

import com.anchorren.async.EventModel;
import com.anchorren.async.EventProducer;
import com.anchorren.async.EventType;
import com.anchorren.model.*;
import com.anchorren.service.CommentService;
import com.anchorren.service.FollowService;
import com.anchorren.service.QuestionService;
import com.anchorren.service.UserService;
import com.anchorren.utils.QAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Controller
public class FollowController {

	@Autowired
	private FollowService followService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private EventProducer eventProducer;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;


	/**
	 * 关注用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "/followUser", method = {RequestMethod.POST})
	@ResponseBody
	public String follow(@RequestParam("userId") int userId) {

		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}
		boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_USER)
				.setEntityId(userId)
				.setEntityOwnerId(userId));

		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setActorId(hostHolder.getUser().getId()).setEntityId(userId)
				.setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

		//返回关注人数
		return QAUtil.getJSONString(result? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
	}

	/**
	 * 取消关注用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "/unfollowUser", method = {RequestMethod.POST})
	@ResponseBody
	public String unFollow(@RequestParam("userId") int userId) {

		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}
		boolean result = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_USER)
				.setEntityId(userId)
				.setEntityOwnerId(userId));
		//返回关注人数
		return QAUtil.getJSONString(result? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
	}

	/**
	 * 关注问题
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path = "/followQuestion", method = {RequestMethod.POST})
	@ResponseBody
	public String followQuestion(@RequestParam("questionId") int questionId) {

		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}

		Question q = questionService.getQuestionById(questionId);
		if (q == null) {
			return QAUtil.getJSONString(1, "问题不存在");
		}

		boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_QUESTION)
				.setEntityId(questionId)
				.setEntityOwnerId(questionId));

		Map<String, Object> info = new HashMap<>();
		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

		return QAUtil.getJSONString(result? 0 : 1, info);
	}

	/**
	 * 取消关注问题
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path = "/unfollowQuestion", method = {RequestMethod.POST})
	@ResponseBody
	public String unfollowQuestion(@RequestParam("questionId") int questionId) {

		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}

		Question q = questionService.getQuestionById(questionId);
		if (q == null) {
			return QAUtil.getJSONString(1, "问题不存在");
		}

		boolean result = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		/*eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_QUESTION)
				.setEntityId(questionId)
				.setEntityOwnerId(questionId));*/

		Map<String, Object> info = new HashMap<>();
		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

		return QAUtil.getJSONString(result? 0 : 1, info);
	}

	/**
	 * 获取粉丝页面
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "/user/{uid}/followers", method = {RequestMethod.GET})
	public String followers(Model model, @PathVariable("uid") int userId) {
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
		}else{
			model.addAttribute("followers", getUsersInfo(0, followerIds));
		}
		model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
		model.addAttribute("curUser", userService.getUser(userId));
		return "followers";
	}

	/**
	 * 关注者页面
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "/user/{uid}/followees", method = {RequestMethod.GET})
	public String followees(Model model, @PathVariable("uid") int userId) {
		List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER,  0, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
		}else{
			model.addAttribute("followees", getUsersInfo(0, followeeIds));
		}
		model.addAttribute("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
		model.addAttribute("curUser", userService.getUser(userId));
		return "followees";
	}

	private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
		List<ViewObject> userInfos = new ArrayList<>();
		for (Integer userId : userIds) {
			User user = userService.getUser(userId);
			if (user == null) {
				continue;
			}
			ViewObject vo = new ViewObject();
			vo.set("user", user);
			vo.set("commentCount", commentService.getCommentCount(EntityType.ENTITY_USER, userId));
			vo.set("followeeCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
			vo.set("followerCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
			if (localUserId != 0) {
				vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, userId));
			}else{
				vo.set("followed", false);
			}
			userInfos.add(vo);
		}
		return userInfos;
	}

}
