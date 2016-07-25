package com.anchorren.service;

import com.anchorren.dao.QuestionDao;
import com.anchorren.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<Question> getLatestQuestions(int userId,int offset,int limit){
		return questionDao.selectLatestQuestions(userId,offset,limit);
	}
}
