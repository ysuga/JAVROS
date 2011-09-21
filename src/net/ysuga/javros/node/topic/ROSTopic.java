/**
 * ROSService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.io.IOException;
import java.net.UnknownHostException;

import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class ROSTopic {

	final private String name;
	final private String type;
	final private String md5sum;
	
	private ROSTopicTypeInfo topicTypeInfo;
	
	public ROSTopicTypeInfo getTopicTypeInfo() {
		return topicTypeInfo;
	}
	
	public void setTopicTypeInfo(ROSTopicTypeInfo info) {
		this.topicTypeInfo = info;
	}
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * @param name
	 * @param type
	 * @param md5sum
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param name
	 * @param type
	 * @param md5sum
	 * </div>
	 */
	public ROSTopic(String name, String type, String md5sum) {
		super();
		this.name = name;
		this.type = type;
		this.md5sum = md5sum;
	}
	
	/**
	 * @return name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return type
	 * @throws RosRefException 
	 * @throws XmlRpcRequestException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ROSServiceNotFoundException 
	 */
	public final String getType() {
		return type;
	}
	
	/**
	 * @return md5sum
	 * @throws RosRefException 
	 * @throws XmlRpcRequestException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ROSServiceNotFoundException 
	 */
	public final String getMd5sum() {
		return md5sum;
	}  
	

	@Override
	public boolean equals(Object o) {
		if(o instanceof ROSTopic) {
			return ((ROSTopic) o).getName().equals(this.getName());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
