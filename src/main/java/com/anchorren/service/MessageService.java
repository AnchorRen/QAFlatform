package com.anchorren.service;

import com.anchorren.dao.MessageDao;
import com.anchorren.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/7/31
 */
@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;

	@Autowired
	SensitiveService sensitiveService;

	/**
	 * 添加一条私信
	 * @param message
	 * @return
	 */
	public int addMessage(Message message) {
		message.setContent(sensitiveService.filter(message.getContent()));
		return messageDao.addMessage(message);
	}

	/**
	 * 查询私信详情
	 * @param conversationId 对话Id
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Message> getConversionDetail(String conversationId,int offset,int limit){
		return messageDao.getConversionDetail(conversationId, offset, limit);
	}

	/**
	 * 查询所有私信
	 * @param userId 用户Id
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Message> getConversationList(int userId, int offset, int limit) {
		List<Message> messageList = messageDao.getConversationList(userId, offset, limit);
		return messageList;
	}

	/**
	 * 获取一个私信中未读信息条数
	 * @param userId
	 * @param conversationId
	 * @return
	 */
	public int getConversationUnReadCount(int userId, String conversationId) {
		return messageDao.getConversationUnReadCount(userId, conversationId);
	}

	public void updateHasRead(int userId, String conversationId) {
		messageDao.updateHasRead(conversationId, userId);
	}
}
