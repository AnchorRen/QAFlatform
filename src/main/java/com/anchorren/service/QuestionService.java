package com.anchorren.service;

import com.anchorren.dao.QuestionDao;
import com.anchorren.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/23
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class QuestionService {

	@Autowired
	QuestionDao questionDao;

	@Autowired
	SensitiveService sensitiveService;

	public List<Question> getLatestQuestions(int userId,int offset,int limit){
		return questionDao.selectLatestQuestions(userId,offset,limit);
	}

	public int addQuestion(Question question) {

		//HTML标签过滤
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

		//敏感词过滤
		question.setContent(sensitiveService.filter(question.getContent()));
		question.setTitle(sensitiveService.filter(question.getTitle()));

		int result = questionDao.addQuestion(question);
		return result > 0 ? question.getId() : 0;
	}

	public Question getQuestionById(int id) {
		Question quesiton = questionDao.getQuestionById(id);
		return quesiton;
	}

	public int updateCommentCount(int id, int count) {
		return questionDao.updateCommmentCount(id, count);
	}
}
