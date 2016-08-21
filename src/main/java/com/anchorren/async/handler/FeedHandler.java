package com.anchorren.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.anchorren.async.EventHandler;
import com.anchorren.async.EventModel;
import com.anchorren.async.EventType;
import com.anchorren.model.*;
import com.anchorren.service.*;
import com.anchorren.utils.JedisUtil;
import com.anchorren.utils.QAUtil;
import com.anchorren.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Component
public class FeedHandler implements EventHandler{

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private FeedService feedService;

	@Autowired
	private FollowService followService;

	@Autowired
	private JedisUtil jedisUtil;

	private String buildData(EventModel model) {
		Map<String, String> map = new HashMap<>();
		User actor = userService.getUser(model.getActorId());
		if (actor == null) {
			return null;
		}
		map.put("userId", String.valueOf(actor.getId()));
		map.put("userHead", actor.getHeadUrl());
		map.put("username", actor.getName());

		if (model.getType() == EventType.COMMENT ||
				(model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
			Question question = questionService.getQuestionById(model.getEntityId());
			if (question == null) {
				return null;
			}
			map.put("quesitonId", String.valueOf(question.getId()));
			map.put("questionTitle", question.getTitle());
			return JSONObject.toJSONString(map);
		}
		return null;
	}

	@Override
	public void doHandle(EventModel model) {

		// 为了测试，把model的userId随机一下
		Random r = new Random();
		model.setActorId(1+r.nextInt(10));

		// 拉模式
		Feed feed = new Feed();
		feed.setCreatedDate(new Date());
		feed.setUserId(model.getActorId());
		feed.setType(model.getType().getValue());
		feed.setData(buildData(model));
		if (feed.getData() == null) {
			return;
		}
		feedService.addFeed(feed);

		//推模式
		List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
		//系统队列
		followers.add(0);
		//给所有粉丝推事件
		for (Integer follower : followers) {
			String timelineKey = RedisKeyUtil.getTimelineKey(follower);
			jedisUtil.lpush(timelineKey, String.valueOf(feed.getId()));
			//限制最大长度，如果timelineKey的长度过大，就删除后面的新鲜事
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
	}
}
