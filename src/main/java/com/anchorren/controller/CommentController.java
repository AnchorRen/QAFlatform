package com.anchorren.controller;

import com.anchorren.dao.QuestionDao;
import com.anchorren.model.Comment;
import com.anchorren.model.EntityType;
import com.anchorren.model.HostHolder;
import com.anchorren.service.CommentService;
import com.anchorren.service.QuestionService;
import com.anchorren.utils.QAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author AnchorRen
 * @date 2016/7/31
 */
@Controller
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	CommentService commentService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	QuestionService questionService;

	@RequestMapping(value = "/addComment",method = {RequestMethod.POST})
	public String addComment(@RequestParam("questionId") int questionId,
							 @RequestParam("content") String content){

		Comment comment = new Comment();
		try {
			if (hostHolder.getUser() != null) {
				comment.setUserId(hostHolder.getUser().getId());
			}else{
				comment.setUserId(QAUtil.ANONYMOUS_USERID);
			}
			comment.setContent(content);
			comment.setCreatedDate(new Date());
			comment.setEntityType(EntityType.ENTITY_QUESTION);
			comment.setStatus(0);
			comment.setEntityId(questionId);
			commentService.addComment(comment);

			//添加评论要更新问题评论的数量
			int commentCount = commentService.getCommentCount(questionId, EntityType.ENTITY_QUESTION);
			questionService.updateCommentCount(questionId,commentCount);


		} catch (Exception e) {
			logger.error("添加评论失败！" + e.getMessage());
		}
		return "redirect:/question/"+questionId;
		//更新

	}
}
