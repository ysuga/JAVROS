/**
 * ROSServiceInfo.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class ROSTopicTypeInfo {
	public Hashtable<String, String> param;

	public ROSTopicTypeInfo() {
		param = new Hashtable<String, String>();
	}

	/**
	 * 
	 * <div lang="ja"> コンストラクタ
	 * 
	 * @param str
	 *            </div> <div lang="en"> Constructor
	 * @param str
	 *            </div>
	 */
	public ROSTopicTypeInfo(String str) {
		this();
		StringTokenizer tokenizer = new StringTokenizer(str);
		while (tokenizer.hasMoreTokens()) {
			String buffer = tokenizer.nextToken();
			param.put(buffer, tokenizer.nextToken());
		}
	}

}
