/**
 * ROSServiceInfo.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.value;

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
public class ROSValueTypeInfo {
	public List<String> typeList;
	public List<String> nameList;

	public ROSValueTypeInfo() {
		typeList = new ArrayList<String>();
		nameList = new ArrayList<String>();
	}

	public void put(String type, String name) {
		typeList.add(type);
		nameList.add(name);
	}
	/**
	 * 
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param str
	 *            </div> <div lang="en"> Constructor
	 * @param str
	 *            </div>
	 */
	public ROSValueTypeInfo(String str) throws ROSValueInvalidTypeInfoException {
		this();
		StringTokenizer tokenizer = new StringTokenizer(str);
		while (tokenizer.hasMoreTokens()) {
			String type = tokenizer.nextToken();
			if(tokenizer.hasMoreTokens()) {
				String name = tokenizer.nextToken();
				typeList.add(type);
				nameList.add(name);
			} else {
				throw new ROSValueInvalidTypeInfoException();
			}
		}
	}

}
