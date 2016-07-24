package com.anchorren.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/23.
 */
public class Message {

	private int id;
	private int fromId;
	private String content;
	private int conversion_id;
	private Date create_date;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getConversion_id() {
		return conversion_id;
	}

	public void setConversion_id(int conversion_id) {
		this.conversion_id = conversion_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", fromId=" + fromId +
				", content='" + content + '\'' +
				", conversion_id=" + conversion_id +
				", create_date=" + create_date +
				'}';
	}
}
