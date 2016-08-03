package com.anchorren.controller;

import com.anchorren.dao.QuestionDao;
import com.anchorren.model.*;
import com.anchorren.service.CommentService;
import com.anchorren.service.LikeService;
import com.anchorren.service.QuestionService;
import com.anchorren.service.UserService;
import com.anchorren.utils.QAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

		User user = userService.getUser(question.getUserId());
		model.addAttribute("user", user);
		model.addAttribute("question", question);

		//查找问题下的所有评论
		List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments = new ArrayList<>();
		for (Comment comment : commentList) {
			ViewObject vo = new ViewObject();
			vo.set("comment", comment);
			vo.set("likeCount",likeService.getLikeCont(EntityType.ENTITY_COMMENT,comment.getId()));
			vo.set("user", userService.getUser(comment.getUserId()));
			if (hostHolder.getUser() == null) {
				vo.set("liked",0);
			}else{
				vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
			}
			comments.add(vo);
		}
		model.addAttribute("comments", comments);

		return "detail";
	}
}
