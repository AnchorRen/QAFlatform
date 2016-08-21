package com.anchorren.controller;

import com.anchorren.model.*;
import com.anchorren.service.CommentService;
import com.anchorren.service.FollowService;
import com.anchorren.service.QuestionService;
import com.anchorren.service.UserService;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/23
 */
@Controller
public class HomeController {

	@Autowired
	UserService userService;

	@Autowired
	QuestionService questionService;

	@Autowired
	FollowService followService;

	@Autowired
	CommentService commentService;

	@Autowired
	HostHolder hostHolder;

	@RequestMapping(path = "/user/{userId}", method = {RequestMethod.GET})
	public String showUserIndex(Model model, @PathVariable("userId") int userId) {
		List<ViewObject> vos = getLatestQuestions(userId, 0, 10);
		model.addAttribute("vos", vos);

		User user = userService.getUser(userId);
		ViewObject vo = new ViewObject();
		vo.set("user", user);
		vo.set("commentCount", commentService.getUserCommentCount(userId));
		vo.set("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
		vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
		if (hostHolder.getUser() != null) {
			vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
		}else{
			vo.set("followed", false);
		}
		model.addAttribute("profileUser", vo);
		return "profile";
	}



	@RequestMapping(path = {"/", "index"}, method = {RequestMethod.GET})
	public String showIndex(Model model) {
		List<ViewObject> vos = getLatestQuestions(0, 0, 10);
		model.addAttribute("vos", vos);
		return "index";
	}

	public  List<ViewObject> getLatestQuestions(int userId, int offset, int limit) {
		List<Question> questions = questionService.getLatestQuestions(userId, 0, 10);
		List<ViewObject> vos = new ArrayList<>();
		for (Question question : questions) {
			ViewObject viewObject = new ViewObject();
			viewObject.set("question", question);
			viewObject.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
			viewObject.set("user", userService.getUser(question.getUserId()));
			vos.add(viewObject);
		}
		return vos;
	}
}
