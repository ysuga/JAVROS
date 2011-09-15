/**
 * ROSServiceInfo.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

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
public class ROSServiceTypeInfo {

	/**
	 * Note:
	 * key -- parameter name
	 * value -- parameter type
	 */

	public Hashtable<String, String> returnParam;

	/**
	 * Note:
	 * key -- parameter name
	 * value -- parameter type
	 */
	public Hashtable<String, String> argumentParam;

	final public static String DIVIDER = "---";

	public ROSServiceTypeInfo() {
		returnParam = new Hashtable<String, String>();
		argumentParam = new Hashtable<String, String>();
	}

	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 * 
	 * @param str
	 */
	public ROSServiceTypeInfo(String str) {
		this();
		StringTokenizer tokenizer = new StringTokenizer(str);
		Hashtable<String, String> targetTable = argumentParam;
		while (tokenizer.hasMoreTokens()) {
			String buffer = tokenizer.nextToken();
			if (buffer.equals(DIVIDER)) {
				targetTable = returnParam;
				continue;
			}
			targetTable.put(tokenizer.nextToken(), buffer);
		}
	}

}
