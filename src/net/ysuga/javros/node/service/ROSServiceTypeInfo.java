/**
 * ROSServiceInfo.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import java.util.StringTokenizer;

import net.ysuga.javros.value.ROSValueInvalidTypeInfoException;
import net.ysuga.javros.value.ROSValueTypeInfo;

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

	public ROSValueTypeInfo argumentInfo;
	
	public ROSValueTypeInfo returnInfo;
	

	final public static String DIVIDER = "---";

	public ROSServiceTypeInfo() {
		this.argumentInfo = new ROSValueTypeInfo();
		this.returnInfo = new ROSValueTypeInfo();
	}

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 * 
	 * @param str
	 * @throws ROSValueInvalidTypeInfoException 
	 */
	public ROSServiceTypeInfo(String str) throws ROSValueInvalidTypeInfoException {
		this();
		StringTokenizer tokenizer = new StringTokenizer(str);
		StringBuilder argumentInfoStr = new StringBuilder();
		StringBuilder returnInfoStr = new StringBuilder();
		
		StringBuilder typeInfo = argumentInfoStr;
		while (tokenizer.hasMoreTokens()) {
			String type = tokenizer.nextToken();
			if (type.equals(DIVIDER)) {
				typeInfo = returnInfoStr;
				break;
			}
			typeInfo.append(type);
			typeInfo.append(" ");
			typeInfo.append(tokenizer.nextToken());
			typeInfo.append("\n");
		}
		
		argumentInfo = new ROSValueTypeInfo(argumentInfoStr.toString());
		returnInfo = new ROSValueTypeInfo(returnInfoStr.toString());
	}

}
