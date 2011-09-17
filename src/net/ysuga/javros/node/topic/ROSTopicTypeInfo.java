/**
 * ROSServiceInfo.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.util.ArrayList;
import java.util.List;
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
	List<String> typeList;
	List<String> nameList;

	public ROSTopicTypeInfo() {
		typeList = new ArrayList<String>();
		nameList = new ArrayList<String>();
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
	public ROSTopicTypeInfo(String str) throws ROSTopicInvalidTypeInfoException {
		this();
		StringTokenizer tokenizer = new StringTokenizer(str);
		while (tokenizer.hasMoreTokens()) {
			String type = tokenizer.nextToken();
			if(tokenizer.hasMoreTokens()) {
				String name = tokenizer.nextToken();
				typeList.add(type);
				nameList.add(name);
			} else {
				throw new ROSTopicInvalidTypeInfoException();
			}
		}
	}

}
