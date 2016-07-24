package com.anchorren.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图类
 * @author AnchorRen
 * @date 2016/7/23
 */
public class ViewObject {

	private Map<String,Object> obj = new HashMap<>();

	public void set(String key, Object value) {
		obj.put(key, value);

	}

	public Object get(String key) {
		return obj.get(key);
	}
}
