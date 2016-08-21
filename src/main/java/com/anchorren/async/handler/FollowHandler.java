package com.anchorren.async.handler;

import com.anchorren.async.EventHandler;
import com.anchorren.async.EventModel;
import com.anchorren.async.EventType;
import com.anchorren.model.EntityType;
import com.anchorren.model.Message;
import com.anchorren.model.User;
import com.anchorren.service.MessageService;
import com.anchorren.service.UserService;
import com.anchorren.utils.QAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Component
public class FollowHandler implements EventHandler{

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;


	@Override
	public void doHandle(EventModel model) {

		Message message = new Message();
		message.setFromId(QAUtil.ANONYMOUS_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		User user = userService.getUser(model.getActorId());

		if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
			message.setContent("用户" + user.getName()
					+ "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
		}else{
			message.setContent("用户" + user.getName()
					+ "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
		}
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW);
	}
}
