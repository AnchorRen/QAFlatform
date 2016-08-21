package com.anchorren.controller;

import com.anchorren.dao.QuestionDao;
import com.anchorren.model.*;
import com.anchorren.service.*;
import com.anchorren.utils.QAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/30
 */
@Controller
public class QuestionController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	QuestionService questionService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	UserService userService;

	@Autowired
	CommentService commentService;

	@Autowired
	LikeService likeService;

	@Autowired
	FollowService followService;


	@RequestMapping(value = "/question/add", method = {RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {

		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			if (hostHolder.getUser() != null) { //用户登录了
				question.setUserId(hostHolder.getUser().getId());
			} else { //用户未登录
				question.setUserId(QAUtil.ANONYMOUS_USERID);
			}
			if (questionService.addQuestion(question) > 0) {
				return QAUtil.getJSONString(0);
			}
		} catch (Exception e) {
			logger.error("增加题目失败：" + e.getMessage());
			e.printStackTrace();

		}
		return QAUtil.getJSONString(1, "失败");
	}

	@RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
	public String getQuestionDetail(Model model, @PathVariable("qid") int qid) {

		Question question = questionService.getQuestionById(qid);
		model.addAttribute("question", question);

		List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments = new ArrayList<ViewObject>();
		for (Comment comment : commentList) {
			ViewObject vo = new ViewObject();
			vo.set("comment", comment);
			if (hostHolder.getUser() == null) {
				vo.set("liked", 0);
			} else {
				vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			}

			vo.set("likeCount", likeService.getLikeCont(EntityType.ENTITY_COMMENT, comment.getId()));
			vo.set("user", userService.getUser(comment.getUserId()));
			comments.add(vo);
		}

		model.addAttribute("comments", comments);

		List<ViewObject> followUsers = new ArrayList<ViewObject>();
		// 获取关注的用户信息
		List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
		for (Integer userId : users) {
			ViewObject vo = new ViewObject();
			User u = userService.getUser(userId);
			if (u == null) {
				continue;
			}
			vo.set("name", u.getName());
			vo.set("headUrl", u.getHeadUrl());
			vo.set("id", u.getId());
			followUsers.add(vo);
		}
		model.addAttribute("followUsers", followUsers);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
		} else {
			model.addAttribute("followed", false);
		}

		return "detail";
	}
}
