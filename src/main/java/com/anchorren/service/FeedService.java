package com.anchorren.service;

import com.anchorren.dao.FeedDao;
import com.anchorren.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
@Service
public class FeedService {

	@Autowired
	private FeedDao feedDao;

	/**
	 * 拉模式
	 * @param maxId 新鲜事最大Id
	 * @param ids 关注者Id集合
	 * @param count 拉取的数量
	 * @return
	 */
	public List<Feed> getUserFeed(int maxId, List<Integer> ids, int count) {
		return feedDao.selectUserFeeds(maxId, ids, count);
	}

	/**
	 * 添加一个Feed
	 * @param feed
	 * @return
	 */
	public boolean addFeed(Feed feed) {
		feedDao.addFeed(feed);
		return feed.getId() > 0;
	}

	public Feed getFeedById(int id) {
		return feedDao.getFeedById(id);
	}
}
