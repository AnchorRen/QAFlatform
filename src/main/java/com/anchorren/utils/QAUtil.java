package com.anchorren.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author AnchorRen
 * @date 2016/7/30
 */
public class QAUtil {

	private static final Logger logger = LoggerFactory.getLogger(QAUtil.class);

	public static int ANONYMOUS_USERID = 3; //匿名ID


	public static String getJSONString(int code,String msg) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg",msg);
		return json.toJSONString();

	}

	public static String getJSONString(int code) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		return json.toJSONString();
	}

	public static String getJSONString(int code, Map<String, Object> map) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			json.put(entry.getKey(), entry.getValue());
		}
		return json.toJSONString();
	}


}
