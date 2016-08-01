package com.anchorren.service;

import com.anchorren.dao.CommentDao;
import com.anchorren.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/31
 */
@Service
public class CommentService {

	@Autowired
	CommentDao commentDao;

	@Autowired
	SensitiveService sensitiveService;

	public List<Comment> getCommentsByEntity(int entityId, int entityType) {
		List<Comment> comments = commentDao.selectByEntity(entityId, entityType);
		return comments;
	}

	public int addComment(Comment comment) {
		//HTML标签过滤
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		//敏感词过滤
		comment.setContent(sensitiveService.filter(comment.getContent()));

		int result = commentDao.addComment(comment);
		return result;
	}

	public int getCommentCount(int entityId, int entityType) {
		int commentCount = commentDao.getCommentCount(entityId, entityType);
		return commentCount;

	}

	public void deleteComment(int entityId, int entityType) {
		commentDao.updateStatus(entityId,entityType,1);
	}

	/*public void deleteComment(int id){
		commentDao.getCommentCount(id, 1);
	}*/
}
