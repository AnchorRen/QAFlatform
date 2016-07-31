package com.anchorren.service;

import com.sun.xml.internal.fastinfoset.algorithm.DoubleEncodingAlgorithm;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤
 *
 * @author AnchorRen
 * @date 2016/7/31
 */
@Service
public class SensitiveService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

	//敏感词替换符号
	private static final String DEFAULT_REPLACEMENT = "***";

	//根节点
	private TrieNode  rootNode = new TrieNode();

	/**
	 * 初始化读取敏感词词汇
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		rootNode = new TrieNode();

		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("" +
					"SensitiveWords.txt");
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String lineTxt;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineTxt = lineTxt.trim();
				//System.out.println(lineTxt);
				addWord(lineTxt);
			}
			reader.close();
		} catch (IOException e) {
			logger.error("读取敏感词文件失败！" + e.getMessage());
		}

	}

	/**
	 * 把关键词添加到前缀树种。
	 * @param lineTxt 要添加的关键词
	 */
	private void addWord(String lineTxt) {
		TrieNode tempNode = rootNode;
		//循环每一个字节
		for (int i = 0; i < lineTxt.length(); i++) {
			Character c = lineTxt.charAt(i);
			//过滤空格等
			if (isSymbol(c)) {
				continue;
			}
			TrieNode node = tempNode.getSubNode(c);
			//还没有初始化
			if (node == null) {
				node = new TrieNode();
				tempNode.addSubNode(c, node);
			}
			//已经初始化了
			tempNode = node;

			if (i == lineTxt.length() - 1) {
				//关键字结束，设置结束标志
				tempNode.setKeywordEnd(true);
			}
		}
	}

	/**
	 * 前缀树节点类
	 */
	private class TrieNode {
		/*
			true:关键词终结，false：继续
		 */
		private boolean end = false;
		/**
		 * key：下一个字符，value：对应的节点
		 */
		private Map<Character, TrieNode> subNodes = new HashMap<>();

		/**
		 * 向指定位置添加节点树
		 */
		void addSubNode(Character key, TrieNode node) {
			subNodes.put(key, node);
		}

		/**
		 * 获取下一个节点
		 */
		TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}

		boolean isKeywordEnd() {
			return  end;
		}

		void setKeywordEnd(boolean end) {
			this.end  = end;
		}

		public int getSubNodeCount() {
			return subNodes.size();
		}

	}

	/**
	 * 判断是否为一个符号
	 */
	private boolean isSymbol(char c) {
		int ic = (int)c;
		//不是英文字母并且也不是东亚文字。0x2E80-0x9FFF 东亚文字范围
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
	}

	/**
	 * 过滤敏感词
	 * @param text 要过滤的内容
	 * @return 过滤后的内容
	 */
	public String filter(String text) {

		if (StringUtils.isBlank(text)) {
			return text;
		}
		String replacement = DEFAULT_REPLACEMENT;
		StringBuffer result = new StringBuffer();

		TrieNode tempNode = rootNode;
		int begin = 0; //回滚数
		int position = 0; //当前比较的位置

		while (position < text.length()) {

			char c = text.charAt(position);
			//特殊直接挑错
			if (isSymbol(c)) {
				//如果是首字符
				if (tempNode == rootNode) {
					result.append(c);
					++begin;
				}
				++position;
				continue;
			}
			//获取以 c 为节点值的子结点
			tempNode = tempNode.getSubNode(c);
			//当前位置的匹配结束了
			if (tempNode == null) {
				//以begin开始的字符串不存在敏感词
				result.append(text.charAt(begin));
				//跳到下一个字符开始测试
				position = begin + 1;
				begin = position;
				//回到树的根节点
				tempNode = rootNode;
			} else if (tempNode.isKeywordEnd()) {
				//发现敏感词了
				result.append(replacement);
				position = position + 1;
				begin = position;
				tempNode = rootNode;
			} else{

				++ position;
			}

		}
		//添加最后的一段字符
		result.append(text.substring(begin));
		return result.toString();

	}

	public static void main(String[] args) {
		SensitiveService sensitiveService = new SensitiveService();
		sensitiveService.addWord("色情");
		sensitiveService.addWord("赌博");
		System.out.println(sensitiveService.filter(" 你好。x 色 情。赌 博"));
	}

}
