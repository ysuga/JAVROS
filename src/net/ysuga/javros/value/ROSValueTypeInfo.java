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
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;


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
		ArrayList<String> premitiveTypes = new ArrayList<String>(Arrays.asList(
				"byte", "bool", "int8", "uint8", "int16", "uint16", "int32", "uint32", "int64", "uint64",
				"float32", "float64", "string", "duration", "time"
				));
		
		StringTokenizer tokenizer = new StringTokenizer(str);
		while (tokenizer.hasMoreTokens()) {
			String type = tokenizer.nextToken();
			if(tokenizer.hasMoreTokens()) {
				String name = tokenizer.nextToken();
				if (!name.contains("=")) {
					if (premitiveTypes.contains(type)) {
						typeList.add(type);
						nameList.add(name);
					} else if (type.endsWith("[]") ){
					    typeList.add(type);
					    nameList.add(name);
					}
				}
			} else {
				throw new ROSValueInvalidTypeInfoException();
			}
		}
	}

}
