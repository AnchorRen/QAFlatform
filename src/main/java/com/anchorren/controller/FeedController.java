package com.anchorren.controller;

import com.anchorren.model.EntityType;
import com.anchorren.model.Feed;
import com.anchorren.model.HostHolder;
import com.anchorren.service.FeedService;
import com.anchorren.service.FollowService;
import com.anchorren.utils.JedisUtil;
import com.anchorren.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Controller
public class FeedController {

	@Autowired
	private FeedService feedService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private FollowService followService;

	@Autowired
	private JedisUtil jedisUtil;

	/**
	 * 拉模式读取
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/pullfeeds", method = {RequestMethod.GET})
	private String getPullFeed(Model model){

		int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
		List<Integer> followees = new ArrayList<>();
		if (localUserId != 0) {
			followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
		}

		List<Feed> feeds = feedService.getUserFeed(Integer.MAX_VALUE, followees, 10);
		model.addAttribute("feeds", feeds);

		return "feeds";
	}

	/**
	 * 推模式
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/pushfeeds", method= {RequestMethod.GET})
	private String getPushFeeds(Model model) {
		int localUserId = hostHolder.getUser()!= null ? hostHolder.getUser().getId() : 0;
		List<String> feedIds = jedisUtil.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 20);
		List<Feed> feeds = new ArrayList<>();
		for (String feedId : feedIds) {
			Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
			if (feed != null) {
				feeds.add(feed);
			}
		}
		model.addAttribute("feeds", feeds);
		return "feeds";
	}
}
