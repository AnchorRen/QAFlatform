package com.anchorren.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author AnchorRen
 * @date 2016/8/20
 */
public class Feed {

	private int id;
	private int type;
	private int userId;
	private Date createdDate;
	private String data;

	private JSONObject dataJson = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
		dataJson = JSONObject.parseObject(data);
	}

	public String get(String key) {
		return dataJson == null ? null : dataJson.getString(key);
	}

	@Override
	public String toString() {
		return "Feed{" +
				"id=" + id +
				", type='" + type + '\'' +
				", userId=" + userId +
				", createdDate=" + createdDate +
				", data='" + data + '\'' +
				'}';
	}
}
