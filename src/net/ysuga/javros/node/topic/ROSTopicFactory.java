/**
 * ROSServiceFactory.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.value.ROSValueInvalidTypeInfoException;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

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
public class ROSTopicFactory {

	static private HashMap<String, ROSTopic> createdROSTopicMap;
	static {
		createdROSTopicMap = new HashMap<String, ROSTopic>();
	}
	
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	private ROSTopicFactory() {
	}

	static public ROSTopic createROSTopic(String topicName) throws RosRefException  {
		if(!createdROSTopicMap.containsKey(topicName)) {
			String topicType = ROSCoreRef.getInstance().getTopicType(topicName);
			ROSTopic topic = new ROSTopic(topicName, topicType, ROSCoreRef.getInstance().getTopicMd5Sum(topicType));
			try {
				topic.setTopicTypeInfo(ROSCoreRef.getInstance().getTopicTypeInfo(topic.getType()));
			} catch (ROSValueInvalidTypeInfoException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
				throw new RosRefException("invalid topic type:" + topicName);
			}
			createdROSTopicMap.put(topicName, topic);
		}
		return createdROSTopicMap.get(topicName);
	}
	
	static public ROSTopic createROSTopic(String topicName, String topicType) throws RosRefException {
		if(!createdROSTopicMap.containsKey(topicName)) {
			ROSTopic topic = new ROSTopic(topicName, topicType, ROSCoreRef.getInstance().getTopicMd5Sum(topicType));
			try {
				topic.setTopicTypeInfo(ROSCoreRef.getInstance().getTopicTypeInfo(topic.getType()));
			} catch (ROSValueInvalidTypeInfoException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
				throw new RosRefException("invalid topic type:" + topicName);
			}
			createdROSTopicMap.put(topicName, topic);
		}
		return createdROSTopicMap.get(topicName);
	}
}
