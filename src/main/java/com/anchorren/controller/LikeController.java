package com.anchorren.controller;

import com.anchorren.model.EntityType;
import com.anchorren.model.HostHolder;
import com.anchorren.service.LikeService;
import com.anchorren.utils.QAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author AnchorRen
 * @date 2016/8/3
 */

@Controller
public class LikeController {

	@Autowired
	LikeService likeService;

	@Autowired
	HostHolder hostHolder;

	/**
	 * 用户赞一个
	 * @param commentId
	 * @return
	 */
	@RequestMapping(path = "/like", method = RequestMethod.POST)
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}

		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT
											, commentId);
		return QAUtil.getJSONString(0, String.valueOf(likeCount));

	}

	/**
	 * 踩
	 * @param commentId
	 * @return
	 */
	@RequestMapping(path = "/dislike", method = RequestMethod.POST)
	@ResponseBody
	public String dislike(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return QAUtil.getJSONString(999);
		}

		long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT
				, commentId);
		return QAUtil.getJSONString(0, String.valueOf(likeCount));

	}


}
